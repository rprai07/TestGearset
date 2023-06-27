System.debug('Test Case for Billing Status moves to 'Pre-Billing Complete');
GHI_UTF_ProstateARV7OrderProcessing.createOrder(); 
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId); 
GHI_UTF_ProstateARV7OrderProcessing.closePreBillingCase($orderId);
List<OrderItem> oli = [select OSM_Billing_Status__c from OrderItem where orderid  = :$orderId];
System.assertEquals('Pre-Billing Complete', oli[0].OSM_Billing_Status__c);
