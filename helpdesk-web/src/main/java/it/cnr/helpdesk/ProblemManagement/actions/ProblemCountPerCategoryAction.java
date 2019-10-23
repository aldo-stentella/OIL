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
 * Created on 5-lug-2005
 *


 */
package it.cnr.helpdesk.ProblemManagement.actions;

import it.cnr.helpdesk.ProblemManagement.exceptions.ProblemJBException;
import it.cnr.helpdesk.ProblemManagement.javabeans.Problem;
import it.cnr.helpdesk.ProblemManagement.valueobjects.*;
import it.cnr.helpdesk.UserManagement.exceptions.UserJBException;
import it.cnr.helpdesk.UserManagement.javabeans.User;

import java.util.*;

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
public class ProblemCountPerCategoryAction extends Action {
    private static Log log = LogFactory.getLog(ProblemCountPerCategoryAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UserJBException, ProblemJBException {
        log.warn("In execute method of ProblemCountPerCategoryAction");
        String Closed = request.getParameter("closed");

        Problem problem = new Problem();
        if ("GET".equals(request.getMethod())) {
            HttpSession se = request.getSession(true);
            String instance = (String)se.getAttribute("it.cnr.helpdesk.instance");
            String closed = request.getParameter("closed");

            if (closed != null) {
                se.setAttribute("it.cnr.helpdesk.closed", closed);
            } else {
                closed = (String) se.getAttribute("it.cnr.helpdesk.closed");
            }
            User us = (User) se.getAttribute("it.cnr.helpdesk.currentuser");
            String login = us.getLogin();
            ProblemCountValueObject pcvobj = new ProblemCountValueObject();
            pcvobj.setExpertLogin(login);
            Collection c = null;
            if (closed.equals("NO")) {
                c = problem.getBacklogProblemCountPerCategory(pcvobj, instance);

            } else if (closed.equals("YES")) {
                c = problem.getClosedProblemCountPerCategory(pcvobj, instance);
            } else if (closed.equals("STO")) {
                // voglio vedere tutti i problemi delle mie categorie anche di altri
                pcvobj.setOwnerShip(false);
                c = problem.getClosedAndVerifiedProblemCountPerCategory(pcvobj, instance);
            } else if (closed.equals("VER")) {
                c = problem.getVerifiedProblemCountPerCategory(pcvobj, instance);
            }
            request.setAttribute("coll",c);
        }
        return mapping.findForward("ProblemCountPerCategory");
    }
}