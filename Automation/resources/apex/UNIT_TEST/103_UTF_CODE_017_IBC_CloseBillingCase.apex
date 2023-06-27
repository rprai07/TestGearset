GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.processOrder($orderId);
GHI_UTF_IBCOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_IBCOrderProcessing.createPackage($orderId);
GHI_UTF_IBCOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_IBCOrderProcessing.createSpecimen($orderId);
GHI_UTF_IBCOrderProcessing.createResult($orderId);
GHI_UTF_IBCOrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_IBCOrderProcessing.createDistributionEvent($orderId);
GHI_UTF_IBCOrderProcessing.closeBillingCase($orderId);
List<Case> billcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Billing'];
System.assertEquals('Closed', billcase[0].Status);


