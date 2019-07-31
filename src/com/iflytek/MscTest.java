package com.iflytek;

import java.util.ArrayList;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.iflytek.cloud.speech.SynthesizerListener;

public class MscTest {
	private static final String APPID = "575e1e64";
	/**
	 * 将字节缓冲区按照固定大小进行分割成数组
	 * 
	 * @param buffer
	 *            缓冲区
	 * @param length
	 *            缓冲区大小
	 * @param spsize
	 *            切割块大小
	 * @return
	 */
	public ArrayList<byte[]> splitBuffer(byte[] buffer, int length, int spsize) {
		ArrayList<byte[]> array = new ArrayList<byte[]>();
		if (spsize <= 0 || length <= 0 || buffer == null
				|| buffer.length < length)
			return array;
		int size = 0;
		while (size < length) {
			int left = length - size;
			if (spsize < left) {
				byte[] sdata = new byte[spsize];
				System.arraycopy(buffer, size, sdata, 0, spsize);
				array.add(sdata);
				size += spsize;
			} else {
				byte[] sdata = new byte[left];
				System.arraycopy(buffer, size, sdata, 0, left);
				array.add(sdata);
				size += left;
			}
		}
		return array;
	}


	/**
	 * 无声合成
	 */
	private static  String Synthesize( String str) {
		SpeechSynthesizer speechSynthesizer = SpeechSynthesizer
				.createSynthesizer();
		// 设置发音人
		speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		// 设置语速，范围0~100
		speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
		// 设置语调，范围0~100
		speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
		// 设置音量，范围0~100
		speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");
		// 设置合成音频保存位置（可自定义保存位置），默认保存在“./iflytek.pcm”
		speechSynthesizer.synthesizeToUri(str, "D:\\audio1.pcm",
				synthesizeToUriListener);
		
		
		String uri = "D:/audio1.pcm";
		System.out.println("uri="+uri);
		return uri;
	}

	/**
	 * 无声合成监听器
	 */
	 static SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {

		public void onBufferProgress(int progress) {
			DebugLog.Log("*************合成进度*************" + progress);

		}

		public void onSynthesizeCompleted(String uri, SpeechError error) {
			if (error == null) {
				DebugLog.Log("*************合成成功*************");
				DebugLog.Log("合成音频生成路径：" + uri);
			} else
				DebugLog.Log("*************" + error.getErrorCode()
						+ "*************");
			
		}

	};
	/**
	 * 有声合成
	 * */
	 private static void audio(String str){
		    //1.创建SpeechSynthesizer对象  
		    SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );  
		    //2.合成参数设置，详见《iFlytek MSC Reference Manual》SpeechSynthesizer 类  
		    mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人  
		    mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速  
		    mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100  
		    //设置合成音频保存位置（可自定义保存位置），保存在“./iflytek.pcm”  
		    //如果不需要保存合成音频，注释该行代码  
		    mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "D:\\audio2.pcm");
		    //3.开始合成  
		     
		    System.out.println("执行语音合成");
		    mTts.startSpeaking(str, mSynListener); 
		    System.out.println("语音播放结束");
	 }
	 
	/**
	*有声合成监听器
	 */  
    private static SynthesizerListener mSynListener = new SynthesizerListener(){  
        //会话结束回调接口，没有错误时，error为null  
        public void onCompleted(SpeechError error) {}  
        //缓冲进度回调  
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。  
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}  
        //开始播放  
        public void onSpeakBegin() {}  
        //暂停播放  
        public void onSpeakPaused() {}  
        //播放进度回调  
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.  
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}  
        //恢复播放回调接口  
        public void onSpeakResumed() {}  
    };  

    //语音合成测试
    public  static void main(String args[]) {
		//在应用发布版本中，请勿显示日志，详情见此函数说明。
		SpeechUtility.createUtility("appid=" + APPID);
		String str = "对方不想跟你说话,并向你扔了一个BUG!";
		//调起语音合成
		//audio(str);
		//调起无声合成
		String uri = Synthesize(str);
		//String url = System.getProperty("user.dir");
		String url ="D:/audio/audio.mp3";
		System.out.println("url="+url);
		ConvertPCMtoMP3 cpm = new ConvertPCMtoMP3();
		try {
			//pcm转换MP3
			cpm.convertAudioFiles(uri, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
