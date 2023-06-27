GHI_UTF_ColonOrderProcessing.createOrder(); 
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
Order order = [select OSM_Status__c from order where id = :$orderId];
System.assertEquals('Processing', order.OSM_Status__c);