package com.study.mqtt;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Test {
	static int maxCount = 1;
	static CountDownLatch countDownLatch = new CountDownLatch(1);
	static CyclicBarrier cyclicBarrier = new CyclicBarrier(maxCount);

	public static void main(String[] args) throws InterruptedException {
		System.out.println();
		
//		
//		// // HelloWorld
//		for (int i = 0; i < maxCount; i++) {
//
////			new Thread(() -> {
//				// try {
//				// cyclicBarrier.await();
//				connectMqtt("test" + UUID.randomUUID() + Thread.currentThread().getName());
//				// } catch (BrokenBarrierException | InterruptedException e) {
//				// // TODO Auto-generated catch block
//				// e.printStackTrace();
//				// }
////			}).start();
//
//		}
//
//		countDownLatch.await();
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

		// mqtt.setUserName("mqtt_root");
		// mqtt.setPassword("sharePassword".toCharArray());

		// mqtt.setMaxInflight(maxInflight);

		// mqtt.setMqttVersion(MqttVersion);
		mqtt.setUserName("admin_api");
		mqtt.setPassword("adminpublic".toCharArray());
//		mqtt.setPassword("adminpublic".toCharArray());

		// mqtt.setUserName("guest");
		// mqtt.setPassword("public".toCharArray());
		// 起到负载均衡和高可用的作用
		// mqtt.setServerURIs(array);
		// mqtt.setSocketFactory(socketFactory);
		// mqtt.setSSLHostnameVerifier(hostnameVerifier);
		// mqtt.setSSLProperties(props);
		// mqtt.setWill(topic, payload, qos, retained);
		// mqtt.setWill(topic, payload, qos, retained);
		try {
			MqttClient mqttClient = new MqttClient("tcp://47.254.75.197:1883", clientId, new MemoryPersistence());
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					mqttClient.disconnect();
					mqttClient.close();
				} catch (MqttException e) {
					System.out.println("关闭mqttClient失败");
				}
			}));
			
			mqttClient.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println(MessageFormat.format("clientId:{0} message:{1}", clientId, message));
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					System.out.println(MessageFormat.format("clientId:{0} token:{1}", clientId, token));

				}

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println(MessageFormat.format("clientId:{0} cause:{1}", clientId, cause));

				}
			});

			mqttClient.connect(mqtt);

			mqttClient.subscribe("$SYS/brokers", (topic, message) -> {
				System.out
						.println(MessageFormat.format("subscribe message clientId:{0} message:{1}", clientId, message));
			});

			//订阅遗愿消息
			mqttClient.subscribe("lastWill/+", (topic, message) -> {
				System.out.println(
						MessageFormat.format("lastwill subscribe message topic:{0} message:{1}", topic, message));
			});

			mqttClient.subscribe("$SYS/brokers/+/clients/+/connected", (topic, message) -> {
				System.out.println(MessageFormat.format("SYS 上线  message topic:{0} message:{1}", topic, message));
			});

			mqttClient.subscribe("$SYS/brokers/+/clients/+/disconnected", (topic, message) -> {
				String[] topics= topic.split("/");
				System.out.println(MessageFormat.format("SYS 下线通知 node:{0} clientId:{0} message:{2} ", topics[2],topics[4], message));
			});

			// 订阅消息时可以用+、#通配符进行订阅主题匹配如：
			// '+': 表示通配一个层级，例如a/+，匹配a/x, a/y
			//
			// '#': 表示通配多个层级，例如a/#，匹配a/x, a/b/c/d
			// 订阅者订阅的主题为a/+或者a/#订阅者的acl请求只会发一次。
			 mqttClient.publish("clients/c_2f071944b5a24c74ad40a6d4895996cc", new MqttMessage("中国强大了333".getBytes()));
			 
			 mqttClient.publish("shareTopic", new MqttMessage("中国强大了testThread-0".getBytes()));

		} catch (MqttException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println(1);
	}

}
