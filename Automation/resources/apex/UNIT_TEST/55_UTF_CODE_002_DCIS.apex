GHI_UTF_DCISOrderProcessing.createOrder(); 
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId); 
Order order = [select OSM_Status__c from order where id = :$orderId];
System.assertEquals('Processing', order.OSM_Status__c);