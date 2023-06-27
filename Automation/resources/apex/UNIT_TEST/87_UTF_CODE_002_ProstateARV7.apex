GHI_UTF_ProstateARV7OrderProcessing.createOrder(); 
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateARV7OrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateARV7OrderProcessing.processOrder($orderId); 
Order order = [select OSM_Status__c from order where id = :$orderId];
System.assertEquals('Processing', order.OSM_Status__c);