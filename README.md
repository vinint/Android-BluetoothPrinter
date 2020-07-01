# Android-BluetoothPrinter
Android蓝牙打印机驱动库

## 使用

添加依赖（Jcenter）：

**Gradle**

```java
implementation 'io.vin.android:bluetoothprinterprotocol:1.0.2'
implementation 'io.vin.android:bluetoothprinter:1.0.18'
```



**Maven**

```java
<dependency>
  <groupId>io.vin.android</groupId>
  <artifactId>bluetoothprinterprotocol</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>

<dependency>
  <groupId>io.vin.android</groupId>
  <artifactId>bluetoothprinter</artifactId>
  <version>1.0.11</version>
  <type>pom</type>
</dependency>

```

**其中bluetoothprinterprotocol库为定义蓝牙打印协议层，bluetoothprinter为协议实现驱动层。**

具体使用如下：

```java
//1.初始化支持的打印机驱动
//启瑞
BluetoothPrinterManager.getInstance().registerPrinter("QR-386", new QRBluetoothPrinterFactory("QR-386"), 3);
BluetoothPrinterManager.getInstance().registerPrinter("QR380", new QRBluetoothPrinterFactory("QR380"), 3);
//汉印
BluetoothPrinterManager.getInstance().registerPrinter("HM-A300", new HprtBluetoothPrinterFactory(""), 2);
BluetoothPrinterManager.getInstance().registerPrinter("HM-A330", new HprtBluetoothPrinterFactory(""), 2);
BluetoothPrinterManager.getInstance().registerPrinter("HM-A318", new HprtBluetoothPrinterFactory(""), 2);
BluetoothPrinterManager.getInstance().registerPrinter("HM-A320", new HprtBluetoothPrinterFactory(""), 2);
BluetoothPrinterManager.getInstance().registerPrinter("HM-A360", new HprtBluetoothPrinterFactory(""), 2);
BluetoothPrinterManager.getInstance().registerPrinter("HM-A350", new HprtBluetoothPrinterFactory(""), 2);
BluetoothPrinterManager.getInstance().registerPrinter("HM-M35", new HprtBluetoothPrinterFactory(""), 2);
//济强
BluetoothPrinterManager.getInstance().registerPrinter("JLP352", new JqBluetoothPrinterFactory(""), 1);
BluetoothPrinterManager.getInstance().registerPrinter("WYH888", new JqBluetoothPrinterFactory(""), 1);
BluetoothPrinterManager.getInstance().registerPrinter("EXP342", new JqBluetoothPrinterFactory("EXP342"), 1);
//芝柯
BluetoothPrinterManager.getInstance().registerPrinter("ZTO588", new ZicoxBluetoothPrinterFactory(""), 4);
BluetoothPrinterManager.getInstance().registerPrinter("XT423", new ZicoxBluetoothPrinterFactory(""), 4);
//快麦
BluetoothPrinterManager.getInstance().registerPrinter("KM-118B", new KuaiMaiBluetoothPrinterFactory("KM-118B"), 1);
BluetoothPrinterManager.getInstance().registerPrinter("KM-218BT", new KuaiMaiBluetoothPrinterFactory("KM-218BT"), 1);

//2.连接打印机
BluetoothPrinterManager.getInstance().connect(printerName, printerAddr)

//3.获取打印机状态(非必须)
int printerStatus = BluetoothPrinterManager.getInstance().printerStatus();
if (printerStatus == IBluetoothPrinterProtocol.STATUS_DISCONNECT) {
}

//4.打印
IBluetoothPrinterProtocol printerProtocol = BluetoothPrinterManager.getInstance().getBluetoothPrinterProtocol();
if (printerProtocol == null) {
  	return;
}
//draw Line
printerProtocol.drawLine(3*8,13*8,3*8,3*8,3,IBluetoothPrinterProtocol.STYLE_LINE_FULL);
printerProtocol.drawLine(3*8,3*8,3*8,13*8,3,IBluetoothPrinterProtocol.STYLE_LINE_FULL);
//draw Text
printerProtocol.drawText(3*8,5*8,0,0,"draw text",FONT_SIZE_32,STYLE_TEXT_NO,COLOR_BLACK,STYLE_ROTATION_0);
//draw Barcode
printerProtocol.drawBarCode(3*8,8*8,10*8,2,"12345678",STYLE_BARCODE_CODE128,STYLE_ROTATION_0);
//draw Qrcode
printerProtocol.drawQRCode(3*8,18*8,"test draw qrcode",QRCODE_UNIT_WIDTH_6,QRCODE_CORRECTION_LEVEL_H,STYLE_ROTATION_0);
printerProtocol.printAndFeed(new PrintCallback() {
            @Override
            public void onPrintFail(int code) {
            }
            @Override
            public void onPrintSuccess() {
            }
        });
```



项目中有printer_instruction_doc文件夹，其中包含了一些常用的蓝牙打印机指令集文档，实际对于蓝牙打印机只要使用的是相同的指令，那就只有打印机包含字体类型、字体大小、获取打印机状态指令有所不同，其他基本相同。文档可以用作参考。

若想支持新的蓝牙打印机可以实现IBluetoothPrinterProtocol、IBluetoothPrinterFactory这两个接口即可。

