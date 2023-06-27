GHI_UTF_PCDxIHCOrderProcessing.createOrder();
//GHI_UTF_PCDxIHCOrderProcessing.updateOrder($orderId,1);
GHI_UTF_PCDxIHCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_PCDxIHCOrderProcessing.createOrderRole('Specimen Submitting');
//GHI_UTF_PCDxIHCOrderProcessing.updateOLI($orderId,1);
GHI_UTF_PCDxIHCOrderProcessing.processOrder($orderId);
GHI_UTF_PCDxIHCOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_PCDxIHCOrderProcessing.createPackage($orderId);
GHI_UTF_PCDxIHCOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_PCDxIHCOrderProcessing.createSpecimen($orderId);
GHI_UTF_PCDxIHCOrderProcessing.createResult($orderId);
GHI_UTF_PCDxIHCOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_PCDxIHCOrderProcessing.createDistributionEvent($orderId);
Order ord= [Select OSM_Status__c from order where Id =:$orderId];
System.assertEquals('Closed', ord.OSM_Status__c);





