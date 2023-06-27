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
Order ord= [Select OSM_Status__c from order where Id =:$orderId];
System.assertEquals('Closed', ord.OSM_Status__c);



