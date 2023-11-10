package br.com.doit.rxworld.mapper;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import br.com.doit.rxworld.repository.StateRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class DOitMapperTest {
	@Inject
	StateRepository stateRepository;
	
//    @Test
//    public void fromRxWorldOrderToDOitSaleOrder() throws Exception {
//    	var rxWorldOrder = new RxWorldOrder();
//    		
//    	rxWorldOrder.buyerName = "John Doe";
//
//        var doitOrder = DOitMapper.toDOitSaleOrder(rxWorldOrder, stateRepository);
//        
//        assertThat(doitOrder.shippingDescription, is("Method Title\nJohn Doe"));
//    }
}