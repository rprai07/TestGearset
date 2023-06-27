GHI_UTF_PCDxFullOrderProcessing.createOrder();
GHI_UTF_PCDxFullOrderProcessing.createOrderRole('Ordering');
GHI_UTF_PCDxFullOrderProcessing.createOrderRole('Specimen Submitting');
//GHI_UTF_PCDxFullOrderProcessing.updateOLI($orderId,1);
//GHI_UTF_PCDxFullOrderProcessing.updateOLI($orderId,2);
GHI_UTF_PCDxFullOrderProcessing.processOrder($orderId);
GHI_UTF_PCDxFullOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_PCDxFullOrderProcessing.createPackage($orderId);
GHI_UTF_PCDxFullOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_PCDxFullOrderProcessing.createSpecimen($orderId);
GHI_UTF_PCDxFullOrderProcessing.createResult($orderId);
GHI_UTF_PCDxFullOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_PCDxFullOrderProcessing.createDistributionEvent($orderId);
Order ord= [Select OSM_Status__c from order where Id =:$orderId];
System.assertEquals('Closed', ord.OSM_Status__c);



