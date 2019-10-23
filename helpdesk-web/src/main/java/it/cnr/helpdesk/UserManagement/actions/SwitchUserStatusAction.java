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
/*
 * Created on 25-giu-2005
 *


 */
package it.cnr.helpdesk.UserManagement.actions;

import it.cnr.helpdesk.UserManagement.exceptions.UserJBException;
import it.cnr.helpdesk.UserManagement.javabeans.User;
import it.cnr.helpdesk.UserManagement.valueobjects.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Aldo Stentella Liberati
 * 


 */
public class SwitchUserStatusAction extends Action {
    private static Log log = LogFactory.getLog(SwitchUserStatusAction.class);
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UserJBException {
		log.warn("In execute method of SwitchUserStatusAction");
		User user = new User();
		HttpSession session = request.getSession(true);
		String instance = (String) session.getAttribute("it.cnr.helpdesk.instance");
		String azione = request.getParameter("azione");
		String conf = request.getParameter("confermato");
		String login = request.getParameter("login");
		user.setLogin(login);
		UserValueObject uvo = user.getDetail(instance);
		if (azione != null) {
			if (conf != null) {
				if (conf.equals("yes")) {
					if (azione.equals("dis")) {
						user.disable(login, uvo.getProfile(), ((User) session.getAttribute("it.cnr.helpdesk.currentuser")).getLogin(), instance);
					} else if (azione.equals("en")) {
						user.enable(login, instance);
					}
					request.setAttribute("param", "close"); // chiudi
					return mapping.findForward("SwitchUserStatus");
				}
			} else {
				if (azione.equals("dis")) {

					if (uvo.getProfile() == 2) {
						request.setAttribute("param", "nodis"); // impossibile disabilitare esperto
						return mapping.findForward("SwitchUserStatus");
					}
				}
				return mapping.findForward("SwitchUserStatus");
			}
		}
		return mapping.findForward("Home");
	}
}