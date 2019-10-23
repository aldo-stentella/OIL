/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.helpdesk.StateMachineManagement.tasks;

import it.cnr.helpdesk.ApplicationSettingsManagement.javabeans.Settings;
import it.cnr.helpdesk.AttachmentManagement.exceptions.AttachmentDAOException;
import it.cnr.helpdesk.AttachmentManagement.javabeans.Attachment;
import it.cnr.helpdesk.AttachmentManagement.valueobjects.AttachmentValueObject;
import it.cnr.helpdesk.EventManagement.exceptions.EventJBException;
import it.cnr.helpdesk.EventManagement.javabeans.Event;
import it.cnr.helpdesk.MailManagement.javabeans.MailManagement;
import it.cnr.helpdesk.MailManagement.valueobjects.MailItem;
import it.cnr.helpdesk.MailParserManagement.exceptions.MailParserJBException;
import it.cnr.helpdesk.MailParserManagement.javabeans.MailParser;
import it.cnr.helpdesk.MailParserManagement.valueobjects.MailComponent;
import it.cnr.helpdesk.ProblemManagement.exceptions.ProblemDAOException;
import it.cnr.helpdesk.ProblemManagement.valueobjects.EventValueObject;
import it.cnr.helpdesk.ProblemManagement.valueobjects.ProblemValueObject;
import it.cnr.helpdesk.StateMachineManagement.exceptions.TaskException;
import it.cnr.helpdesk.UserManagement.exceptions.UserDAOException;
import it.cnr.helpdesk.UserManagement.javabeans.User;
import it.cnr.helpdesk.UserManagement.valueobjects.UserValueObject;
import it.cnr.helpdesk.dao.AttachmentDAO;
import it.cnr.helpdesk.dao.DAOFactory;
import it.cnr.helpdesk.dao.ProblemDAO;
import it.cnr.helpdesk.dao.UserDAO;
import it.cnr.helpdesk.exceptions.SettingsJBException;
import it.cnr.helpdesk.util.MessageComposer;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

/**
 * @author aldo stentella
 * 
 */
public class Attachment2ExternalUserTask extends Task {
	
	public void doAction(Object token) throws TaskException {
		EventValueObject evo = (EventValueObject)token;
		if(evo.getEventType()!=Event.AGGIUNTA_ALLEGATO) return;
		UserDAO userdao = DAOFactory.getDAOFactoryByProperties().getUserDAO();
		ProblemDAO problemdao = DAOFactory.getDAOFactoryByProperties().getProblemDAO();
		SimpleDateFormat itForm = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
		UserValueObject uvo = null;
		UserValueObject ovo = null;
		try {
			problemdao.createConnection(evo.getInstance());
			problemdao.setIdSegnalazione(evo.getIdSegnalazione());
			ProblemValueObject pvo = problemdao.getProblemDetail();
			problemdao.closeConnection();
			//evento 'aggiunta allegato' originato da esperto o validatore su di una segnalazione originata da utente esperto
			if(pvo.getOriginatore().equalsIgnoreCase(User.MAIL_USER) && evo.getOriginatoreEvento()!=User.MAIL_USER){	
				MailParser mp = new MailParser();
				MailComponent temp = mp.getByIdSegnalazione(pvo.getIdSegnalazione(), evo.getInstance());
				uvo=new UserValueObject("","",temp.getNome(),temp.getCognome(),1,temp.getMail(),"","","y","","n","");
				userdao.createConnection(evo.getInstance());
				userdao.setLogin(evo.getOriginatoreEvento());
				ovo = userdao.getUserDetail();
				userdao.closeConnection();
				HashMap map = new HashMap();
				map.put("idSegnalazione", new Long(evo.getIdSegnalazione()));
				map.put("titolo", evo.getTitle());
				map.put("descrizione", evo.getDescription2HTML());
				map.put("categoria",evo.getCategoryDescription());
				map.put("originatoreEventoNome", ovo.getFirstName()+" "+ovo.getFamilyName());
				map.put("originatoreEventoEmail", ovo.getEmail());
				map.put("originatoreEventoTelefono", ovo.getTelefono());
				map.put("stato", evo.getStateDescription());
				map.put("nota", evo.getNote2HTML());
				map.put("originatoreProblemaNome", uvo.getFirstName()+" "+uvo.getFamilyName());
				map.put("originatoreProblemaEmail", uvo.getEmail());
				map.put("originatoreProblemaTelefono", uvo.getTelefono());
				map.put("data_apertura", ((EventValueObject)(new Event()).getProblemEvents(pvo.getIdSegnalazione(),true, evo.getInstance()).iterator().next()).getTime().substring(0,10));
				map.put("data_odierna", itForm.format(new Date()));
				map.put("instance", evo.getInstance());
				map.put("contextPath",  Settings.getProperty("it.oil.contextPath", evo.getInstance()));
				Attachment attachment = new Attachment();
				Collection<AttachmentValueObject> files = attachment.fetchAttachments(evo.getIdSegnalazione(), evo.getInstance());
				byte[] bytes = {};
				String name = null;
				for (Iterator<AttachmentValueObject> iterator = files.iterator(); iterator.hasNext();) {
					AttachmentValueObject attachmentValueObject = iterator.next();
					if(evo.getNote().equalsIgnoreCase("Aggiunto allegato: "+attachmentValueObject.getNomeFile())){
						map.put("descrizioneFile", attachmentValueObject.getDescrizione());
						AttachmentDAO attachmentdao = DAOFactory.getDAOFactoryByProperties().getAttachmentDAO();
						attachmentdao.createConnection(evo.getInstance());
						InputStream is = attachmentdao.getBlobFile(attachmentValueObject.getId());
						bytes =	IOUtils.toByteArray(is);
						attachmentdao.closeConnection();
						name = attachmentValueObject.getNomeFile();
						break;
					}
				}
				if(name != null) {
					Collection destinatari = new ArrayList();
					destinatari.add(uvo);
					String template = Event.getTemplate(evo.getEventType(), evo.getInstance());
					MailItem mailitem;
					mailitem = MessageComposer.compose(map, destinatari, template, evo.getInstance());
					mailitem.setAttachment(bytes);
					mailitem.setFileName(name);
					MailManagement mm = new MailManagement();
					mm.sendMail(mailitem);
				}
			}
		} catch (ProblemDAOException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getUserMessage());
		} catch (UserDAOException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getUserMessage());
		} catch (EventJBException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getUserMessage());
		} catch (MailParserJBException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getUserMessage());
		} catch (SettingsJBException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getUserMessage());
		} catch (AttachmentDAOException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getUserMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e.getMessage());
		}
	}
	
}
