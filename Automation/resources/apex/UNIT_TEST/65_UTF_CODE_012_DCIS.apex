System.debug('Test Case for Billing Status moves to Pre-Billing Complete');
GHI_UTF_DCISOrderProcessing.createOrder(); 
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId); 
GHI_UTF_DCISOrderProcessing.closePreBillingCase($orderId);
List<OrderItem> oli = [select OSM_Billing_Status__c from OrderItem where orderid  = :$orderId];
System.assertEquals('Pre-Billing Complete', oli[0].OSM_Billing_Status__c);
