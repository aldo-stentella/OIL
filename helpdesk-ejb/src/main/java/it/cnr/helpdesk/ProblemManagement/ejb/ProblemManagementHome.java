/*
 * Generated by XDoclet - Do not edit!
 */
package it.cnr.helpdesk.ProblemManagement.ejb;

/**
 * Home interface for ProblemManagement.
 * @generated 
 * @lomboz generated
 */
public interface ProblemManagementHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/ProblemManagement";
   public static final String JNDI_NAME="comp/env/ejb/ProblemManagement";

   public it.cnr.helpdesk.ProblemManagement.ejb.ProblemManagement create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}