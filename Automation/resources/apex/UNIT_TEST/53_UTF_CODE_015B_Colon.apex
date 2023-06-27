System.debug('This will fail in dev environment');
GHI_UTF_ColonOrderProcessing.createOrder(); 
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
GHI_UTF_ColonOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ColonOrderProcessing.createPackage($orderId);
GHI_UTF_ColonOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_ColonOrderProcessing.createSpecimen($orderId);
GHI_UTF_ColonOrderProcessing.createResult($orderId);
GHI_UTF_ColonOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_ColonOrderProcessing.createDistributionEvent($orderId);
List<OSM_Distribution_Event__c> distEvent = [select Id from OSM_Distribution_Event__c WHERE OSM_Order_ID__c  =:$orderId];
System.assert(distEvent.size()>0);




