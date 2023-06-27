System.debug('Test Case for Billing Status moves to Pre-Billing Complete');
GHI_UTF_ColonOrderProcessing.createOrder(); 
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
GHI_UTF_ColonOrderProcessing.closePreBillingCase($orderId);
List<OrderItem> oli = [select OSM_Billing_Status__c from OrderItem where orderid  = :$orderId];
System.assertEquals('Pre-Billing Complete', oli[0].OSM_Billing_Status__c);
