System.debug('Test Case for Billing Status moves to Pre-Billing Complete');
GHI_UTF_ProstateOrderProcessing.createOrder(); 
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
GHI_UTF_ProstateOrderProcessing.closePreBillingCase($orderId);
List<OrderItem> oli = [select OSM_Billing_Status__c from OrderItem where orderid  = :$orderId];
System.assertEquals('Pre-Billing Complete', oli[0].OSM_Billing_Status__c);
