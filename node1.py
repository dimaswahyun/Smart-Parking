import MySQLdb as mdb
import paho.mqtt.client as mqtt
import paho.mqtt.publish as pub
from datetime import datetime
lists = []
db = mdb.connect(host='suksessidang.com', user='suksessi_admin',passwd='@Admin123', db='suksessi_parking')
                                    

# prepare a cursor object using cursor  method
cursor = db.cursor() 

sql = "select * from histori where node = 'A1' AND status_booking = 1 "
cursor.execute(sql)
results = cursor.fetchall() 
for row in results:
	id_node = row[0]
	nama_node = row[1]
	username = row[2]
	waktu_masuk = row[3]
	waktu_keluar = row[4]
	status_booking = row[5]
	status_parkir = row[6]
	if nama_node == "A1" and status_booking ==1:
		print "LAMPU A1 NYALA "
# The callback for when the client receives a CONNACK response from the server.
broker="broker.hivemq.com"
port=8000

db.close()
def on_publish(client,userdata,result):
	print ("sukses publish")


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("node1")
    

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
	
	#try:#print(msg.topic+" "+str(msg.payload))
	
	ss = int(msg.payload)
		#ss = sa
		
	cs = client.subscribe("node1")

	if ss > 7 :
		db = mdb.connect(host='suksessidang.com', user='suksessi_admin',passwd='@Admin123', db='suksessi_parking')
		# prepare a cursor object using cursor  method
		cursor = db.cursor() 
		sql = "select * from histori where node = 'A1' order by id_histori desc limit 1 "
		cursor.execute(sql)
		results = cursor.fetchall() 
		for row in results:
			id_node = row[0]
			nama_node = row[1]
			username = row[2]
			waktu_masuk = row[3]
			waktu_keluar = row[4]
			status_booking = row[5]
			status_parkir = row[6]
			
			lists.append(ss)
			print(lists)
			#mobil datang sebelum waktu habis
			if len(lists) > 60 and status_booking == 1 and status_parkir == 0:
				idn = str(id_node)
				sql2 = "UPDATE histori SET status_booking=0,status_parkir = 0 WHERE id_histori ="+idn
				cursor.execute(sql2)
				db.commit()
				print "MOBIL BATAL PARKIR IF"
				del lists[:]
				#publish mqtt mematikan lampu
			#mobil keluar dari parkir
			elif len(lists) > 3 and status_booking == 1 and status_parkir == 1:
				idn = str(id_node)
				now = datetime.now()
				timestamp = ('%02d-%02d-%02d %02d:%02d:%02d'%(now.year,now.month,now.day,now.hour,now.minute,now.second))
				print(timestamp)
				sql2 = """UPDATE histori SET status_booking=0,status_parkir=0,waktu_keluar=%s WHERE id_histori =%s"""
				
				cursor.execute(sql2, (timestamp,idn))
				db.commit()
				pub = mqtt.Client()
				pub.connect("192.168.43.114",1883,60)
				pub.publish("status1", "off")
				del lists[:]
				print "MOBIL KELUAR ELIF"
				#publish mqtt mematikan lampu
			elif status_booking == 0 and status_parkir == 0 :
				pub = mqtt.Client()
				pub.connect("192.168.43.114",1883,60)
				pub.publish("status1", "off")
				del lists[:]
			else:
				#idn = str(id_node)
				#sql2 = "UPDATE histori SET status_booking=0,status_parkir = 1 WHERE id_histori ="+idn
				#cursor.execute(sql2)
				#db.commit()
				pub = mqtt.Client()
				pub.connect("192.168.43.114",1883,60)
				pub.publish("status1", "on")
				print "MOBIL DALAM PERJALANAN"
				
		db.close()
	elif ss < 7 :
		now = datetime.now()
		timestamp = ('%02d-%02d-%02d %02d:%02d:%02d'%(now.year,now.month,now.day,now.hour,now.minute,now.second))
		print(timestamp)
		a = now.strftime('%Y-%m-%d %H:%M:%S')
		db = mdb.connect(host='suksessidang.com', user='suksessi_admin',passwd='@Admin123', db='suksessi_parking')
								

		# prepare a cursor object using cursor  method
		cursor = db.cursor() 

		sql = "select * from histori where node = 'A1' AND status_booking = 1 "
		cursor.execute(sql)
		results = cursor.fetchall() 
		for row in results:
			id_node = row[0]
			nama_node = row[1]
			username = row[2]
			waktu_masuk = row[3]
			waktu_keluar = row[4]
			status_booking = row[5]
			status_parkir = row[6]
			
			print(lists)
			if status_booking == 1:
				idn = str(id_node)
				sql2 = """UPDATE histori SET status_booking='1',status_parkir='1' WHERE id_histori =%s"""
				
				cursor.execute(sql2, (idn))
				db.commit()
				print "STATUS SEDANG PARKIR"
				#publish mqtt mematikan lampu
		db.close()
			
	else:
		print(lists)
		del lists[:]	
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message


client.connect("192.168.43.114", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()


