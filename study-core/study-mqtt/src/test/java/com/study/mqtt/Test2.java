package com.study.mqtt;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.study.common.util.IdUtil;

public class Test2 {
	static int maxCount = 200;
	static CountDownLatch countDownLatch = new CountDownLatch(2);
	static CyclicBarrier cyclicBarrier = new CyclicBarrier(maxCount);

	static ExecutorService executor=Executors.newFixedThreadPool(maxCount);
	
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < maxCount; i++) {

			executor.execute(() -> {
				try {
					cyclicBarrier.await();
					connectMqtt(UUID.randomUUID().toString());
				} catch (InterruptedException | BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}

		countDownLatch.await();
	}

	private static void connectMqtt(String clientId) {
		MqttConnectOptions mqtt = new MqttConnectOptions();
		// 连接丢失，是否进行重连
		mqtt.setAutomaticReconnect(true);
		// 是否在重启后删除会话
		mqtt.setCleanSession(true);
		// 设置超时时间
		mqtt.setConnectionTimeout(10);
		// 设置心跳间隔
		mqtt.setKeepAliveInterval(20);
		// 设置遗愿消息
		mqtt.setWill("lastWill/" + clientId, "异常退出".getBytes(), 1, true);
		// mqtt.setUserName("mqtt_root");
		// mqtt.setPassword("sharePassword".toCharArray());

		// mqtt.setMaxInflight(maxInflight);

		// mqtt.setMqttVersion(MqttVersion);
		// mqtt.setUserName("admin");
		// mqtt.setPassword("cjj1!ECS".toCharArray());

		mqtt.setUserName("guest");
		mqtt.setPassword("public".toCharArray());
		// 起到负载均衡和高可用的作用
		// mqtt.setServerURIs(array);
		// mqtt.setSocketFactory(socketFactory);
		// mqtt.setSSLHostnameVerifier(hostnameVerifier);
		// mqtt.setSSLProperties(props);
		// mqtt.setWill(topic, payload, qos, retained);
		// mqtt.setWill(topic, payload, qos, retained);
		try {
			MqttClient mqttClient = new MqttClient("tcp://47.254.75.197:1883", clientId, new MemoryPersistence());

			mqttClient.setCallback(new MqttCallbackExtended() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println(MessageFormat.format("topic:{0} message:{1}", topic, message));
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					System.out.println(MessageFormat.format("token:{0}", token));

				}

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println(MessageFormat.format("cause:{0}", cause));

				}
				
				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					// 订阅私有主题
					if(reconnect) {
						subscribe(clientId, mqttClient);
					}
					
				}
			});
			
			mqttClient.connect(mqtt);

			//
			// mqttClient.subscribe("$SYS/brokers", (topic, message) -> {
			// System.out
			// .println(MessageFormat.format("subscribe message clientId:{0} message:{1}",
			// clientId, message));
			// });

			// 订阅私有主题
			subscribe(clientId, mqttClient);

			// mqttClient.subscribe("$SYS/brokers/+/clients/+/connected", (topic, message)
			// -> {
			// System.out.println(MessageFormat.format("SYS 上线 message clientId:{0}
			// topic:{1} message:{2}",
			// clientId, topic, message));
			// });
			//
			// mqttClient.subscribe("$SYS/brokers/+/clients/+/disconnected", (topic,
			// message) -> {
			// System.out.println(MessageFormat.format("SYS 下线 message clientId:{0}
			// topic:{1} message:{2}",
			// clientId, topic, message));
			// });

			// 订阅消息时可以用+、#通配符进行订阅主题匹配如：
			// '+': 表示通配一个层级，例如a/+，匹配a/x, a/y
			//
			// '#': 表示通配多个层级，例如a/#，匹配a/x, a/b/c/d
			// 订阅者订阅的主题为a/+或者a/#订阅者的acl请求只会发一次。
//			 mqttClient.publish("shareTopic", new MqttMessage("中国强大了".getBytes()));

		} catch (MqttException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void subscribe(String clientId, MqttClient mqttClient) {
		String clientTopic = "clients/" + clientId;
		try {
			mqttClient.subscribe(clientTopic,2);
			// 订阅共享主题
			mqttClient.subscribe("shareTopic", (topic, message) -> {
				System.out
						.println(MessageFormat.format("subscribe message clientId:{0} message:{1}", clientId, message));
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
