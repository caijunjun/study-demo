package com.study.mqtt;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Test2 {
	static int maxCount = 1000;
	static CountDownLatch countDownLatch = new CountDownLatch(1);
	static CyclicBarrier cyclicBarrier = new CyclicBarrier(maxCount);

	public static void main(String[] args) throws InterruptedException {
		System.out.println(TimeUnit.MILLISECONDS.toHours(Integer.MAX_VALUE));
		System.out.println(596 / 24);
		// // HelloWorld
		for (int i = 0; i < maxCount; i++) {

			new Thread(() -> {
				// try {
				// cyclicBarrier.await();
				connectMqtt("test" + Thread.currentThread().getName());
				// } catch (BrokenBarrierException | InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}).start();

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
			mqttClient.setCallback(new MqttCallback() {

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
			});

			mqttClient.connect(mqtt);

			//
			// mqttClient.subscribe("$SYS/brokers", (topic, message) -> {
			// System.out
			// .println(MessageFormat.format("subscribe message clientId:{0} message:{1}",
			// clientId, message));
			// });

			// 订阅私有主题
			String clientTopic = "clients/" + clientId;
			mqttClient.subscribe(clientTopic);
			// 订阅共享主题
			mqttClient.subscribe("shareTopic", (topic, message) -> {
				System.out
						.println(MessageFormat.format("subscribe message clientId:{0} message:{1}", clientId, message));
			});

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
		System.out.println(1);
	}

}
