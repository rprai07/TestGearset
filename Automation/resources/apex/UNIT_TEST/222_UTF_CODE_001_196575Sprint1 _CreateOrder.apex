GHI_UTF_PCDxFullOrderProcessing.createOrder();
GHI_UTF_PCDxFullOrderProcessing.createOrderRole('Ordering');
GHI_UTF_PCDxFullOrderProcessing.createOrderRole('Specimen Submitting');
List<OrderItem> oli = [Select Id,OSM_PCDx_Stage_Rec_Rel_Ref_Met_or_Adv__c from OrderItem where OrderId =:$orderId];
GHI_UTF_PCDxFullOrderProcessing.processOrder($orderId);
GHI_UTF_PCDxFullOrderProcessing.updateOLI($orderId,0);
GHI_UTF_PCDxFullOrderProcessing.updateOLI($orderId,1);
List<Case> cases = [Select Id, OSM_Primary_Order__c , Owner.Name from Case where OSM_Primary_Order__c = :$orderId And (Type = 'Missing Data' Or Type = 'Clinical Experience')];
System.assert(cases.size() == 2 && cases[0].Owner.Name == 'PCDx – Billing' && cases[1].Owner.Name == 'PCDx - Missing Data');
