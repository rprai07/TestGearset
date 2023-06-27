GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.processOrder($orderId);
Case pbcase = [Select Id, CaseNumber, Description, OSM_Primary_Order__c, Type from Case where Type = 'Pre-Billing' AND OSM_Primary_Order__c=:$orderId limit 1];
OrderItem oli = [Select OSM_Order_Line_Item_ID__c from OrderItem where OrderId =:$orderId limit 1];

