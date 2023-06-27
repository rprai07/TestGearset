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
Order ord= [Select OSM_Status__c from order where Id =:$orderId];
System.assertEquals('Closed', ord.OSM_Status__c);



