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
Order ord= [Select OSM_Status__c from order where Id =:$orderId];
System.assertEquals('Closed', ord.OSM_Status__c);



