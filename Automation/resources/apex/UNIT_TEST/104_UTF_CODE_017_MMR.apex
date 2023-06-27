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
GHI_UTF_MMROrderProcessing.closeBillingCase($orderId);
List<Case> billcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Billing'];
System.assertEquals('Closed', billcase[0].Status);


