GHI_UTF_MMROrderProcessing.createOrder(); 
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
Order order = [select OSM_Status__c from order where id = :$orderId];
System.assertEquals('Processing', order.OSM_Status__c);