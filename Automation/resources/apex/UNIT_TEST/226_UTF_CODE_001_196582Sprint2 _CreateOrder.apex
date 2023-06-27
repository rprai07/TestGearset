GHI_UTF_IBCOrderProcessing.createOrder(); 
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
Order currOrder = [Select Id, OSM_Created_By_User__c from Order where Id =:$orderId];
currOrder.OSM_Created_By_User__c = '0052f000001ElMl';
update currOrder;
GHI_UTF_IBCOrderProcessing.processOrder($orderId); 
List<Case> eoicase = [select  CaseNumber from Case where OSM_Primary_Order__c   = :$orderId AND Type = 'External Order Intake' limit 1];
System.assert(eoicase != null);