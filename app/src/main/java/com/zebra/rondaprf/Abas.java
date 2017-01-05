package com.zebra.rondaprf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TextView;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


/**
 * Created by Fabio on 08/10/16.
 */

public class Abas extends AppCompatActivity {
    //    TabHost tabHost;
    private Connection connection;
    private Button tcButton;
    private Button datButton;
    private Button batButton;
    private ZebraPrinter printer;
    private TextView statusPrint;
    private static String bluetoothAddressKey = "";
    //   private TabLayout tabLayout;
    public String cpclConfigLabel;
    public String tcTexto;
    public String tcEndereco;
    public String tcTexto2;
    NumberPicker np;
    private DBManager dbManager;

    private Bitmap m_bmp;
    private ParseBitmap m_BmpParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_dat_bat);

        np = (NumberPicker) findViewById(R.id.vias_edat);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(false);
        dbManager = new DBManager(this);
        dbManager.open();
        String dadosUsuario;
        dadosUsuario=dbManager.getUsuario();

        EditText nomeAgente = (EditText) findViewById(R.id.editText_nomeAgente);
        EditText matriculaAgente = (EditText) findViewById(R.id.editText_matricula);
        nomeAgente.setText(dadosUsuario.split(";")[0]);
        matriculaAgente.setText(dadosUsuario.split(";")[1]);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDither = false;
        m_bmp = BitmapFactory.decodeResource(getResources(), R.raw.brasaobmp2, options);
        m_BmpParser = new ParseBitmap(m_bmp);
        final String str = m_BmpParser.ExtractGraphicsDataForCPCL(0, 0);

        bluetoothAddressKey = dbManager.getMac("1");
        final EditText campo_data_ocorrencia = (EditText) findViewById(R.id.editText_data);
        campo_data_ocorrencia.addTextChangedListener(Mask.insert("##/##/####", campo_data_ocorrencia));

        final EditText campo_hora_ocorrencia = (EditText) findViewById(R.id.editText_time);
        campo_hora_ocorrencia.addTextChangedListener(Mask.insert("##:##", campo_hora_ocorrencia));

        final EditText campo_cpf = (EditText) findViewById(R.id.editText_cpf);
        campo_cpf.addTextChangedListener(Mask.insert("###.###.###-##", campo_cpf));

        final EditText campo_telefone = (EditText) findViewById(R.id.editText_fone);
        campo_telefone.addTextChangedListener(Mask.insert("(##)####-####", campo_telefone));


//        statusPrint = (TextView) this.findViewById(R.id.statusImpTc);
        //      statusPrint = (TextView) this.findViewById(R.id.statusImpEdat);
        //    statusPrint = (TextView) this.findViewById(R.id.statusImpBat);

        tcButton = (Button) this.findViewById(R.id.bt_print_tc);
        tcButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {

                        tcTexto =  ((EditText)findViewById(R.id.editText_nomeAgente)).getText().toString()+", Policial Rodoviario Federal, matricula: "+((EditText)findViewById(R.id.editText_matricula)).getText().toString()+", com fulcro na Lei no 9.099/95:"+
                                       "Faz saber a "+((EditText)findViewById(R.id.editText_nomeAutor)).getText().toString()+", RG no "+((EditText)findViewById(R.id.editText_rg)).getText().toString()+", CPF "+((EditText)findViewById(R.id.editText_cpf)).getText().toString()+", residente no endereco "+((EditText)findViewById(R.id.editText_end)).getText().toString()+", que foi lavrado o Termo Circunstanciado no C"+((EditText)findViewById(R.id.editText_matricula)).getText().toString()+"160917175200, e que por este instrumento assume o compromisso de comparecer ao:";
                        tcEndereco = " JUIZADO ESPECIAL CRIMINAL da LINHARES/ES, localizado no endereco Rua Alair Garcia Duarte, s/n, Tres Barras, CEP 29906-660, LINHARES/ES,";
                        tcTexto2 = "em dia e hora a serem determinados posteriormente quando da intimacao, feita pelo referido juízo na forma da lei, na qualidade de autor dos fatos."+
                                     "Fica ciente que o nao comparecimento o sujeitara as medidas previstas na Lei no 9.099/95, bem como devera comparecer acompanhado de advogado, sendo que na sua falta, ser-lhe-a designado defensor publico.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ";


                        cpclConfigLabel = "! 0 200 200 800 1\r\n" + "\r\n" +
                                        "ON-FEED IGNORE\r\n" +
                                        "LINE 0 0 850 0 1\r\n" +
                                        "LINE 200 0 200 255 1\r\n" +
                                        "LINE 0 255 855 255 1\r\n" +
                                        "SETEP 0\r\n" +
                                        "T 7 0 350 15 MINISTERIO DA JUSTICA\r\n" +
                                        "T 7 0 225 34 DEPARTAMENTO DE POLICIA RODOVIARIA FEDERAL\r\n" +
                                        "T 0 2 225 98 TERMO DE COMPROMISSO E COMPARECIMENTO DO AUTOR DO FATO\r\n" +

                               /*         "T 5 0 20 260 "+ tcTexto.substring(0,60) +"\r\n" +
                                        "T 5 0 20 285 "+ tcTexto.substring(60,120) +"\r\n" +
                                        "T 5 0 20 310 "+ tcTexto.substring(120,180) +"\r\n" +
                                        "T 5 0 20 335 "+ tcTexto.substring(180,240) +"\r\n" +
                                        "T 5 0 20 360 "+ tcTexto.substring(240,300) +"\r\n" +
                                        "T 5 0 20 385 "+ tcTexto.substring(300,360) +"\r\n" +
                                        "T 5 0 20 410 "+ tcTexto.substring(360,420) +"\r\n" +
                                        "T 5 0 20 435 "+ tcTexto.substring(420,480) +"\r\n" +
                                        "T 5 0 20 460 "+ tcTexto.substring(480,540) +"\r\n" +
                                        "T 5 0 20 485 "+ tcTexto.substring(540,600) +"\r\n" +
                                        "T 5 0 20 510 "+ tcTexto.substring(600,660) +"\r\n" +
                                        "T 5 0 20 535 "+ tcTexto.substring(660,720) +"\r\n" +
                                        "T 5 0 20 560 "+ tcTexto.substring(720,780) +"\r\n" +
                                        "T 5 0 20 585 "+ tcTexto.substring(780,840) +"\r\n" +
                                        "T 5 0 20 610 "+ tcTexto.substring(840,900) +"\r\n" +
*/
                                "! U1 setvar \"device.languages\" \"line_print\"\r\\n! U1 BEGIN-PAGE\r\n"+tcTexto+" "+tcEndereco+" "+tcTexto2+
                                "! U1 SETVAR \"device.languages\" \"hybrid_xml_zpl\"\r\n! U1 END-PAGE\r\n"+


                                str + "\r\n" +
                                "PRINT\r\n";


                        statusPrint = (TextView) findViewById(R.id.statusImpTc);
                        enableBatButton(false);
                        enableEdatButton(false);
                        enableTcButton(false);
                        Looper.prepare();
                        doConnectionTest();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            }
        });

        datButton = (Button) this.findViewById(R.id.bt_print_edat);
        datButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        cpclConfigLabel = "! 0 200 200 500 " + ((NumberPicker) findViewById(R.id.vias_edat)).getValue() + "\r\n" +
                                "ON-FEED IGNORE\r\n" +
                                "LINE 0 0 850 0 1\r\n" +
                                "LINE 200 0 200 400 1\r\n" +
                                "LINE 0 400 855 400 1\r\n" +
                                "SETEP 0\r\n" +
                                "T 7 0 350 15 MINISTERIO DA JUSTICA\r\n" +
                                "T 7 0 225 34 DEPARTAMENTO DE POLICIA RODOVIARIA FEDERAL\r\n" +
                                "T 5 0 225 65 DECLARACAO DE ACIDENTES:\r\n" +
                                "T 7 0 230 88 Siga as instrucoes:\r\n" +
                                "T 7 0 230 110 A Policia Rodoviaria Federal lamenta  o  acidente\r\n" +
                                "T 7 0 230 130 ocorrido com V.Sra. Informamos que  a  partir  de\r\n" +
                                "T 7 0 230 150 agora o BAT (Boletim de Acidente de Transito)  de\r\n" +
                                "T 7 0 230 170 acidente sem vitima em rodovias federais com  ate\r\n" +
                                "T 7 0 230 190 05 (cinco)  veiculos  pode  ser  registrado  pelo \r\n" +
                                "T 7 0 230 210 proprio usuario pela internet atraves do endereco\r\n" +
                                "T 7 0 230 230 www.prf.gov.br/acidente. A PRF nunca  deixara  de \r\n" +
                                "T 7 0 230 250 atender o cidadao.  Essa mudanca de  procedimento \r\n" +
                                "T 7 0 230 270 se  refere  apenas  ao  registro  de  Declaracao,\r\n" +
                                "T 7 0 230 290 visando agilizar e desburocratizar o atendimento.\r\n" +
                                "T 7 0 230 310 As duvidas mais frequentes podem ser tiradas em:\r\n" +
                                "T 7 0 230 340 www.prf.gov.br/declarante/publico/duvidas.jsf\r\n" +
                                "LINE 0 380 850 380 1\r\n" +
                                str + "\r\n" +
                                "PRINT\r\n";

                        statusPrint = (TextView) findViewById(R.id.statusImpEdat);
                        enableBatButton(false);
                        enableEdatButton(false);
                        enableTcButton(false);
                        Looper.prepare();
                        doConnectionTest();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            }
        });

        batButton = (Button) this.findViewById(R.id.bt_print_bat);
        batButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        cpclConfigLabel =  //4 1 n,4 2 n, 5 0 s, 7 1 s, 5 2 n, 0 1 s,
                                "! 0 200 200 500 2\r\n" + "\r\n" +
                                        "ON-FEED IGNORE\r\n" +
                                        "LINE 0 0 850 0 1\r\n" +
                                        "LINE 200 0 200 255 1\r\n" +
                                        "LINE 0 255 855 255 1\r\n" +
                                        "SETEP 0\r\n" +
                                        "T 7 0 350 15 MINISTERIO DA JUSTICA\r\n" +
                                        "T 7 0 225 34 DEPARTAMENTO DE POLICIA RODOVIARIA FEDERAL\r\n" +
                                        "T 5 0 225 63 COMUNICACAO DE ACIDENTE ->\r\n" +
                                        "BOX 640 60 780 107 1\r\n" +
                                        "T 7 1 645 61 C " + ((EditText) findViewById(R.id.comunicacao)).getText().toString() + "\r\n" +
                                        "T 7 0 230 90 Siga as instrucoes:\r\n" +
                                        "T 7 0 230 110 1-Acesse o sitio http://www.prf.gov.br\r\n" +
                                        "T 7 0 230 130 2-Selecione ATENDIMENTO A ACIDENTES\r\n" +
                                        "T 7 0 230 150 3-Clique em BOLETIM DE ACIDENTE DE TRANSITO\r\n" +
                                        "T 7 0 230 170 4-Informe o numero da Comunicacao e CPF\r\n" +
                                        "T 7 0 240 190 Entao clique no botao CONSULTAR\r\n" +
                                        "T 7 1 230 210 Em caso de duvidas ligue 191\r\n" +
                                        "T 7 0 20 265 Atencao:\r\n" +
                                        "T 7 0 20 285 Para acionar o SEGURO DPVAT acesse o sitio www.dpvatseguro.com.br\r\n" +
                                        "LINE 0 380 850 380 1\r\n" +
 /*
                                       "T 0 0 20 20  teste TESTE 00\r\n" +
                                       "T 0 1 20 40  teste TESTE 01\r\n" +
                                       "T 0 2 20 60  teste TESTE 02\r\n" +
                                       "T 0 3 20 80  teste TESTE 03\r\n" +
                               //        "T 4 0 20 100  teste TESTE 40\r\n" +
                               //        "T 4 1 20 120  teste TESTE 41\r\n" +
                                       "T 5 0 20 140  teste TESTE 50\r\n" +
                              //         "T 5 1 20 160  teste TESTE 51\r\n" +
                                       "T 7 0 20 180  teste TESTE 70\r\n" +
                                       "T 7 1 20 200  teste TESTE 71\r\n" +
                                       "T 7 2 20 220  teste TESTE 72\r\n" +
                                 //      "T 5 2 20 240  teste TESTE 52\r\n" +
*/
                                        str + "\r\n" +
                                        "PRINT\r\n";

                        statusPrint = (TextView) findViewById(R.id.statusImpBat);
                        enableBatButton(false);
                        enableEdatButton(false);
                        enableTcButton(false);
                        Looper.prepare();
                        doConnectionTest();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            }
        });


        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("TAB TC");
        spec.setContent(R.id.tab_tc);
        spec.setIndicator("TC");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("TAB E-Dat");
        spec.setContent(R.id.tab_edat);
        spec.setIndicator("E-Dat");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab BAT");
        spec.setContent(R.id.tab_bat);
        spec.setIndicator("BAT");
        host.addTab(spec);
    }

    public void selData(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selTime(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void enableTcButton(final boolean enabled) {
//        statusPrint = (TextView) this.findViewById(R.id.statusImpTc);
        runOnUiThread(new Runnable() {
            public void run() {
                tcButton.setEnabled(enabled);
            }
        });
    }

    private void enableEdatButton(final boolean enabled) {
//        statusPrint = (TextView) this.findViewById(R.id.statusImpEdat);
        runOnUiThread(new Runnable() {
            public void run() {
                datButton.setEnabled(enabled);
            }
        });
    }

    private void enableBatButton(final boolean enabled) {
//        statusPrint = (TextView) this.findViewById(R.id.statusImpBat);
        runOnUiThread(new Runnable() {
            public void run() {
                batButton.setEnabled(enabled);
            }
        });
    }

    private byte[] getConfigLabel() {
        byte[] configLabel = null;
        try {
//            PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
            SGD.SET("device.languages", "zpl", connection);

//            if (printerLanguage == PrinterLanguage.ZPL) {
//                configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDPRF^FS^XZ".getBytes();
//            } else if (printerLanguage == PrinterLanguage.CPCL) {
            configLabel = cpclConfigLabel.getBytes();

        } catch (ConnectionException e) {

        }
        return configLabel;
    }


    public void doConnectionTest() {
        printer = connect();

        if (printer != null) {
            sendTestLabel();
        } else {
            disconnect();
        }
    }

    private byte[] decodeText(String text, String encoding) throws CharacterCodingException, UnsupportedEncodingException {
        Charset charset = Charset.forName(encoding);
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
        CharBuffer cbuf = decoder.decode(bbuf);
        String s = cbuf.toString();
        return s.getBytes(encoding);
    }

    private void sendTestLabel() {
        try {
            ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer);

            PrinterStatus printerStatus = (linkOsPrinter != null) ? linkOsPrinter.getCurrentStatus() : printer.getCurrentStatus();

            if (printerStatus.isReadyToPrint) {
                byte[] configLabel = getConfigLabel();
                connection.write(configLabel);
                setStatus("Enviando Dados", Color.BLUE);
            } else if (printerStatus.isHeadOpen) {
                setStatus("Tampa Aberta", Color.RED);
            } else if (printerStatus.isPaused) {
                setStatus("Impressão em Pausa", Color.RED);
            } else if (printerStatus.isPaperOut) {
                setStatus("Impressora sem Papel", Color.RED);
            }
            DemoSleeper.sleep(1500);
            if (connection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) connection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                DemoSleeper.sleep(500);
            }
        } catch (ConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect();
        }
    }

    public ZebraPrinter connect() {
        setStatus("Conectando...", Color.YELLOW);
        connection = null;
        connection = new BluetoothConnection(bluetoothAddressKey);
        SettingsHelper.saveBluetoothAddress(this, bluetoothAddressKey);

        try {
            connection.open();
            setStatus("Conectado", Color.GREEN);
        } catch (ConnectionException e) {
            setStatus("Erro! Disconectando", Color.RED);
            DemoSleeper.sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (connection.isConnected()) {
            try {

                printer = ZebraPrinterFactory.getInstance(connection);
                setStatus("Determinando Linguagem", Color.YELLOW);
                String pl = SGD.GET("device.languages", connection);
                setStatus("Linguagem " + pl, Color.BLUE);
            } catch (ConnectionException e) {
                setStatus("Linguagem Desconhecida", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                setStatus("Linguagem Desconhecida", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {
            setStatus("Desconectando", Color.RED);
            if (connection != null) {
                connection.close();
            }
            setStatus("Desconectado", Color.RED);
        } catch (ConnectionException e) {
            setStatus("Erro! Desconectando", Color.RED);
        } finally {
            enableTcButton(true);
            enableEdatButton(true);
            enableBatButton(true);
        }
    }

    private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusPrint.setBackgroundColor(color);
                statusPrint.setText(statusMessage);
            }
        });
        DemoSleeper.sleep(1000);
    }

}
