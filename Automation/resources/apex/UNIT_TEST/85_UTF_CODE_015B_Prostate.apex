System.debug('This will fail in dev environment');
GHI_UTF_ProstateOrderProcessing.createOrder(); 
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
GHI_UTF_ProstateOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ProstateOrderProcessing.createPackage($orderId);
GHI_UTF_ProstateOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_ProstateOrderProcessing.createSpecimen($orderId);
GHI_UTF_ProstateOrderProcessing.closeClinicalExperienceCase($orderId);
GHI_UTF_ProstateOrderProcessing.createResult($orderId);
GHI_UTF_ProstateOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_ProstateOrderProcessing.createDistributionEvent($orderId);
List<OSM_Distribution_Event__c> distEvent = [select Id from OSM_Distribution_Event__c WHERE OSM_Order_ID__c  =:$orderId];
System.assert(distEvent.size()>0);




