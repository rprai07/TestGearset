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
GHI_UTF_ColonOrderProcessing.closeBillingCase($orderId);
List<Case> billcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Billing'];
System.assertEquals('Closed', billcase[0].Status);
