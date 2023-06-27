System.debug('This will fail in dev environment');
GHI_UTF_IBCOrderProcessing.createOrder(); 
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.processOrder($orderId); 
GHI_UTF_IBCOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_IBCOrderProcessing.createPackage($orderId);
GHI_UTF_IBCOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_IBCOrderProcessing.createSpecimen($orderId);
GHI_UTF_IBCOrderProcessing.createResult($orderId);
GHI_UTF_IBCOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_IBCOrderProcessing.createDistributionEvent($orderId);
List<OSM_Distribution_Event__c> distEvent = [select Id from OSM_Distribution_Event__c WHERE OSM_Order_ID__c  =:$orderId];
System.assert(distEvent.size()>0);




