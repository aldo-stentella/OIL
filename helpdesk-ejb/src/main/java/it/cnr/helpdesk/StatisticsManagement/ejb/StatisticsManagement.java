/*
 * Generated by XDoclet - Do not edit!
 */
package it.cnr.helpdesk.StatisticsManagement.ejb;

/**
 * Remote interface for StatisticsManagement.
 * @generated 
 * @lomboz generated
 */
public interface StatisticsManagement
   extends javax.ejb.EJBObject
{

   public java.util.Collection getProblemsDistributionByStatus( it.cnr.helpdesk.StatisticsManagement.valueobjects.StatisticsDTO statisticsdto, java.lang.String instance )
      throws it.cnr.helpdesk.StatisticsManagement.exceptions.StatisticsEJBException, java.rmi.RemoteException;

   public it.cnr.helpdesk.StatisticsManagement.valueobjects.OverallStatisticsDTO getOverallStatistics( it.cnr.helpdesk.StatisticsManagement.valueobjects.StatisticsDTO statisticsdto, java.lang.String instance )
      throws it.cnr.helpdesk.StatisticsManagement.exceptions.StatisticsEJBException, java.rmi.RemoteException;

   public java.util.Collection getProblemsDistributionByCategory( it.cnr.helpdesk.StatisticsManagement.valueobjects.StatisticsDTO statisticsdto, java.lang.String instance )
      throws it.cnr.helpdesk.StatisticsManagement.exceptions.StatisticsEJBException, java.rmi.RemoteException;

}
