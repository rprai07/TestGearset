GHI_UTF_PCDxIHCOrderProcessing.createOrder();
GHI_UTF_PCDxIHCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_PCDxIHCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_PCDxIHCOrderProcessing.processOrder($orderId);
List<Case> cases = [Select Id, Type from Case where Type = 'Pre-Billing'];
List<OrderItem> olis = [Select Id OSM_BI_Ready__c from OrderItem where OrderId = :$orderId and OSM_BI_Ready__c=true];
System.debug('cicd---->'+cases.size());
System.debug('olis---->'+olis.size());
System.assert(olis.size()==2);


