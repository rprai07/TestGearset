System.debug('This will fail in dev environment');
GHI_UTF_DCISOrderProcessing.createOrder(); 
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId); 
GHI_UTF_DCISOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_DCISOrderProcessing.createPackage($orderId);
GHI_UTF_DCISOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_DCISOrderProcessing.closeClinicalExperienceCase($orderId);
GHI_UTF_DCISOrderProcessing.createSpecimen($orderId);
GHI_UTF_DCISOrderProcessing.createResult($orderId);
GHI_UTF_DCISOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_DCISOrderProcessing.createDistributionEvent($orderId);
List<OSM_Distribution_Event__c> distEvent = [select Id from OSM_Distribution_Event__c WHERE OSM_Order_ID__c  =:$orderId];
System.assert(distEvent.size()>0);




