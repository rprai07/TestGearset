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
GHI_UTF_DCISOrderProcessing.closeBillingCase($orderId);
List<Case> billcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Billing'];
System.assertEquals('Closed', billcase[0].Status);

