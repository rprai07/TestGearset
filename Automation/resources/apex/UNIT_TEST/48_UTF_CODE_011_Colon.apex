GHI_UTF_ColonOrderProcessing.createOrder();
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId); 
List<Case> prebilling = [Select Id from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assert(prebilling.size()>0);