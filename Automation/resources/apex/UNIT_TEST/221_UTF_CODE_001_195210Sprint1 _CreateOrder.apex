GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
List<OrderItem> oli = [Select Id,OSM_PCDx_Cancer_Type__c, OSM_PCDx_Other_Cancer_Type__c from OrderItem where OrderId =:$orderId limit 1];
System.assert(oli[0].OSM_PCDx_Cancer_Type__c == 'NSCLC' && oli[0].OSM_PCDx_Other_Cancer_Type__c == 'Testing');