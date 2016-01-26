package tool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;



public class HttpClientHelper {
	private  CloseableHttpClient client = null;

	public HttpClientHelper(String baseUrl, String encoder) {
		createClient();
	}

	private void createClient() {
		client = HttpClientBuilder.create().build();
	}
	/**
	 * 读取HTML
	 * @param url
	 * @return
	 */
	public String html(String url) {
		try {
			
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = client.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                HttpEntity e = response.getEntity();
                ContentType type =  ContentType.getOrDefault(e);
                String content = EntityUtils.toString(e,"UTF-8");
                return content;
            }catch (Exception e) {
				e.printStackTrace();
			}
            finally {
                response.close();
            }
        }catch (Exception e) {
			e.printStackTrace();
		} finally {
		
        }
		return null;
	}
	public String testCloud(String url) {
		String ex="noex";
		try {
			HttpGet get = new HttpGet(url);
			 
            CloseableHttpResponse response = client.execute(get);
            try {
                System.out.println("----------------------------------------");
                HttpEntity e = response.getEntity();
                ContentType type =  ContentType.getOrDefault(e);
                String content = EntityUtils.toString(e,"UTF-8");
                System.out.println(content);
                return "content"+content;
            }catch (Exception e) {
				e.printStackTrace();
			}
            finally {
                response.close();
            }
        }catch (Exception e) {
			e.printStackTrace();
			ex="ex:"+e.toString();
		} finally {
        }
		return ex;
	}
	public String testCloud_p(String url,JSONObject p) {
		
		try {
			
			/*PostMethod method = new PostMethod(url);
			String transJson = p.toString();
			RequestEntity se = new StringRequestEntity(transJson, "application/json", "UTF-8");
			method.setRequestEntity(se);
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			DefaultHttpClient httpClient = new DefaultHttpClient();*/
			
			/*HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, params.get(key)));  
	        }  
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
			
			String encoderJson = URLEncoder.encode(p.toString(), HTTP.UTF_8);
		        
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(url);
	        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
	        
	        StringEntity se = new StringEntity(encoderJson);
	        se.setContentType("text/json");
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httpPost.setEntity(se);
	        httpClient.execute(httpPost);*/
			
			//POST对象
            HttpPost post = new HttpPost(url);// http://pan.lancygroup.com/ucdisk/api/2.0/user/login
            System.out.println("向云盘发送请求: " + post.getRequestLine());
            
            //装载JSON参数
            StringEntity se = new StringEntity(p.toString());// {"clientKey":"0","userName":"test","passWord":"123456"}
            post.setEntity(se);
            System.out.println("向云盘发送参数: " + p);
            //发送请求 并获取结果对象*/
            CloseableHttpResponse response = client.execute(post);
            try {
                System.out.println("由云盘返回的请求----------------------------------------");
                HttpEntity e = response.getEntity();
                ContentType type =  ContentType.getOrDefault(e);
                String content = EntityUtils.toString(e,"UTF-8");
                System.out.println(content);
                System.out.println("由云盘返回的请求----------------------------------------");
                return content;
            }catch (Exception e) {
				e.printStackTrace();
			}
            finally {
                response.close();
            }
        }catch (Exception e) {
			e.printStackTrace();
		} finally {
		
        }
		return null;
	}
	
	public String spiderHelper(String url){
		CloseableHttpClient client2 =HttpClientBuilder.create().build();
		String ex="noex";
		try {
			HttpGet get = new HttpGet(url);
            CloseableHttpResponse response = client2.execute(get);//!
            HttpEntity e = response.getEntity();
            try {
                e = response.getEntity();
                ContentType type =  ContentType.getOrDefault(e);
                String content = EntityUtils.toString(e,"UTF-8");
                //System.out.println();
                return content;
            }catch (Exception ex1) {
				ex1.printStackTrace();
			}
            finally {
            	get.abort();
            	response.close();
            }
        }catch (Exception e) {
			e.printStackTrace();
			ex="ex:"+e.toString();
		} finally {
			try {
				client2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return ex;
	}
	public String testCloud_m(String url, Map<String, String> param) {
		try {
			
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<String> keySet = param.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, param.get(key)));  
	        }  
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
			
            CloseableHttpResponse response = client.execute(httpPost);
            try {
                System.out.println("由云盘返回的请求----------------------------------------");
                HttpEntity e = response.getEntity();
                ContentType type =  ContentType.getOrDefault(e);
                String content = EntityUtils.toString(e,"UTF-8");
                System.out.println(content);
                System.out.println("由云盘返回的请求----------------------------------------");
                return content;
            }catch (Exception e) {
				e.printStackTrace();
			}
            finally {
                response.close();
            }
        }catch (Exception e) {
			e.printStackTrace();
		} finally {
		
        }
		return null;
	}
	public String testCloud_f(String url, Map<String, String> param) {
		try {
			
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<String> keySet = param.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, param.get(key)));  
	        }  
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
			
            CloseableHttpResponse response = client.execute(httpPost);
            try {
                System.out.println("由云盘返回的请求----------------------------------------");
                HttpEntity e = response.getEntity();
                ContentType type =  ContentType.getOrDefault(e);
                String content = EntityUtils.toString(e,"UTF-8");
                System.out.println(content);
                System.out.println("由云盘返回的请求----------------------------------------");
                return content;
            }catch (Exception e) {
				e.printStackTrace();
			}
            finally {
                response.close();
            }
        }catch (Exception e) {
			e.printStackTrace();
		} finally {
		
        }
		return null;
	}

	/**
	 * Download images,pdf,flash etc.
	 * 
	 * @param url
	 * @param file
	 * @return
	 */
	public static void main(String[] args) {
		String url = "http://192.168.13.24:8080/电商/LANCY/153/ALC133WOP016//ALC133WOP016B/tS-9491.jpg";
		String u2 = "http://crm.lancygroup.com/rest/brm/mdm/customerdocuments";
		HttpClientHelper help = new HttpClientHelper(u2, "utf-8");
		help.html(u2);
		/*try {
			URL httpUrl = new URL(url);
			URI uri = new URI(httpUrl.getProtocol(), httpUrl.getHost()+":8080", httpUrl.getPath(), httpUrl.getQuery(), null);
			System.out.println(uri);
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}
		File ff = new File("D:\\work\\workspace\\LancyOMS\\public\\upload\\ALA133WSK231Y\\款式图2011x256.jpg");
		try {
			FileOutputStream out = new FileOutputStream(ff);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	public   boolean file(String url, File file) {
		if(url==null||url.equals(""))
			return false;
		System.out.println(url);
		url = url.replaceAll("\\\\", "/").replaceAll(" ","");
		
		HttpGet httpget = new HttpGet(url);
		boolean result = true;
		CloseableHttpResponse response = null;
		FileOutputStream out = null;
		InputStream in = null;
		try {
			response = client.execute(httpget);
			 in = response.getEntity().getContent();
		    out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			createClient();
			result = false;
		} finally {
			try {
				if(out!=null)
					out.close();
				if(in!=null)
					in.close();
				httpget.releaseConnection();
				if(response!=null)
					response.close();
				if(client!=null)
					client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			result = false;
		}
		return result;
	}

	public boolean file(String url) {
		int i = url.lastIndexOf('/');
		String fileName = url.substring(i + 1);
		return file(url, new File(fileName));
	}
}
