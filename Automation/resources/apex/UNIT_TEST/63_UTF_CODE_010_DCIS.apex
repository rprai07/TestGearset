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
List<OSM_Result__c> results = [Select Id from OSM_Result__c where Order_ID__c = :$orderId];
System.assert(results.size()>0);