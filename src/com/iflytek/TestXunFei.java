package com.iflytek;

import com.autozi.common.core.utils.ApplicationPropertiesHelper;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizeToUriListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class TestXunFei {
    public static void doGet(HttpServletRequest request, HttpServletResponse response, String readText)
            throws Exception {

        request.setCharacterEncoding("UTF-8");//解决乱码

        //合成监听器
        SynthesizeToUriListener synthesizeToUriListener = XunfeiLib.getSynthesize();

        String fileName= ApplicationPropertiesHelper.getImgFilePath()+"tts_test_"+ System.currentTimeMillis()+".pcm";
//        String fileName= "./src/main/resources/tts_test.pcm";
        XunfeiLib.delDone(fileName);

        //1.创建SpeechSynthesizer对象
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100

        //3.开始合成
        //设置合成音频保存位置（可自定义保存位置），默认保存在“./tts_test.pcm”
        mTts.synthesizeToUri(readText,fileName ,synthesizeToUriListener);

        //设置最长时间
        int timeOut=30;
        int star=0;

        //校验文件是否生成
        while(!XunfeiLib.checkDone(fileName)){

            try {
                Thread.sleep(1000);
                star++;
                if(star>timeOut){
                    throw new Exception("合成超过"+timeOut+"秒！");
                }
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                break;
            }

        }

        sayPlay(fileName, request, response);
    }
    /**
     * 将音频内容输出到请求中
     *
     * @param fileName
     * @param request
     * @param response
     */
    public static void sayPlay (String fileName, HttpServletRequest request, HttpServletResponse response) {

        //输出 wav IO流
        try{

            response.setHeader("Content-Type", "audio/mpeg");
            File file = new File(fileName);
            int len_l = (int) file.length();
            byte[] buf = new byte[2048];
            FileInputStream fis = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            //写入WAV文件头信息
            out.write(XunfeiLib.getWAVHeader(len_l,8000,2,16));

            len_l = fis.read(buf);
            while (len_l != -1) {
                out.write(buf, 0, len_l);
                len_l = fis.read(buf);
            }
            out.flush();
            out.close();
            fis.close();

            //删除文件和清除队列信息
            XunfeiLib.delDone(fileName);
            file.delete();
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
