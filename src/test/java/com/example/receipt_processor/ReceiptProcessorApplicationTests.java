package com.example.receipt_processor;

import com.example.receipt_processor.model.Receipt;
import com.example.receipt_processor.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReceiptProcessorApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testReceiptProcessingAndPoints() {

		Receipt receipt = new Receipt();
		receipt.setRetailer("Target");
		receipt.setPurchaseDate("2022-01-01");
		receipt.setPurchaseTime("13:01");
		receipt.setTotal("35.35");


		Item item1 = new Item();
		item1.setShortDescription("Mountain Dew 12PK");
		item1.setPrice("6.49");

		Item item2 = new Item();
		item2.setShortDescription("Emils Cheese Pizza");
		item2.setPrice("12.25");

		Item item3 = new Item();
		item3.setShortDescription("Knorr Creamy Chicken");
		item3.setPrice("1.26");

		Item item4 = new Item();
		item4.setShortDescription("Doritos Nacho Cheese");
		item4.setPrice("3.35");

		Item item5 = new Item();
		item5.setShortDescription("Klarbrunn 12-PK 12 FL OZ");
		item5.setPrice("12.00");

		receipt.setItems(Arrays.asList(item1, item2, item3, item4, item5));


		ResponseEntity<Map> postResponse = restTemplate.postForEntity("/receipts/process", receipt, Map.class);
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Map<String, Object> postBody = postResponse.getBody();
		assertThat(postBody).containsKey("id");


		String receiptId = (String) postBody.get("id");


		ResponseEntity<Map> getResponse = restTemplate.getForEntity("/receipts/" + receiptId + "/points", Map.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Map<String, Object> getBody = getResponse.getBody();
		assertThat(getBody).containsKey("points");


		Integer points = (Integer) getBody.get("points");
		assertThat(points).isEqualTo(28);
	}
}
