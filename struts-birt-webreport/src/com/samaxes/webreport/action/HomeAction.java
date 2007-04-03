/*
 * $Id$
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.samaxes.webreport.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;

import com.samaxes.webreport.common.Constants;

/**
 * Actions related to the home page.
 * 
 * @author : samaxes
 * @version : $Revision$
 */
public class HomeAction extends MappingDispatchAction {

    /**
     * The home action. <br />
     * Gives the choise to how generate the report.
     * 
     * @param actionMapping ActionMapping
     * @param actionForm ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward homeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        return actionMapping.findForward(Constants.FORWARD_SUCCESS);
    }

}
