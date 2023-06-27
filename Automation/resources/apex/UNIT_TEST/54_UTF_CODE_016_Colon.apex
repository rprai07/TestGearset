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
Order ord= [Select OSM_Status__c from order where Id =:$orderId];
System.assertEquals('Closed', ord.OSM_Status__c);



