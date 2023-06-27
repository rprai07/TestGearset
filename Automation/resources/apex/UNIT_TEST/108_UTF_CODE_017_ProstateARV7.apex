GHI_UTF_ProstateARV7OrderProcessing.createOrder();
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId);
GHI_UTF_ProstateARV7OrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createPackage($orderId);
GHI_UTF_ProstateARV7OrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createSpecimen($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createResult($orderId);
GHI_UTF_ProstateARV7OrderProcessing.updateOLIToDVC($orderId);
GHI_UTF_ProstateARV7OrderProcessing.createDistributionEvent($orderId);
GHI_UTF_ProstateARV7OrderProcessing.closeBillingCase($orderId);
List<Case> billcase = [select Status from Case where OSM_Primary_Order__c  = :$orderId AND Type = 'Billing'];
System.assertEquals('Closed', billcase[0].Status);


