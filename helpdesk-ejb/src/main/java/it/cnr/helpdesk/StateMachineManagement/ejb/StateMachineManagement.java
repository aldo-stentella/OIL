/*
 * Generated by XDoclet - Do not edit!
 */
package it.cnr.helpdesk.StateMachineManagement.ejb;

/**
 * Remote interface for StateMachineManagement.
 * @generated 
 * @lomboz generated
 */
public interface StateMachineManagement
   extends javax.ejb.EJBObject
{

   public void readConfiguration( it.cnr.helpdesk.StateMachineManagement.StateMachine stateMachine, java.lang.String instance )
      throws it.cnr.helpdesk.StateMachineManagement.exceptions.StateMachineEJBException, java.rmi.RemoteException;

}