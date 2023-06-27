System.debug('This will fail in dev environment');
GHI_UTF_MMROrderProcessing.createOrder(); 
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
GHI_UTF_MMROrderProcessing.closePreBillingCase($orderId);
GHI_UTF_MMROrderProcessing.createPackage($orderId);
GHI_UTF_MMROrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_MMROrderProcessing.createSpecimen($orderId);
GHI_UTF_MMROrderProcessing.createResult($orderId);
GHI_UTF_MMROrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_MMROrderProcessing.createDistributionEvent($orderId);
List<OSM_Distribution_Event__c> distEvent = [select Id from OSM_Distribution_Event__c WHERE OSM_Order_ID__c  =:$orderId];
System.assert(distEvent.size()>0);




