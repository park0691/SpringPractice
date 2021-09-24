package springpractice.template.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    /*
    템플릿으로 분리한 부분을 제외한 나머지 코드를 BufferedReaderCallback 인터페이스로 만든 익명 내부 클래스에 담는다.
    처리할 파일의 경로와 함께 준비된 익명 내부 클래스의 오브젝트를 템플릿에 전달한다.
    템플릿이 리턴하는 값을 최종 결과로 사용한다.
     */
    public Integer calcSum(String filePath) throws IOException {
        BufferedReaderCallback sumCallback =
                new BufferedReaderCallback() {
                    @Override
                    public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                        Integer sum = 0;
                        String line = null;
                        while ((line = br.readLine()) != null) sum += Integer.valueOf(line);
                        return sum;
                    }
                };
        return fileReadTemplate(filePath, sumCallback);
    }

    public Integer calcSum2(String filePath) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            Integer sum = 0;
            String line = null;
            while ((line = br.readLine()) != null) sum += Integer.valueOf(line);

            br.close();
            return sum;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try { br.close(); }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer calcMultiply(String filePath) throws IOException {
        LineCallback<Integer> multiplyCallback =
                new LineCallback<Integer>() {
                    @Override
                    public Integer doSomethingWithLine(String line, Integer value) {
                        return value * Integer.valueOf(line);
                    }
                };
        return lineReadTemplate(filePath, multiplyCallback, 1);
    }

    public String concatenate(String filePath) throws IOException {
        LineCallback<String> concatenateCallback =
                new LineCallback<String>() {
                    @Override
                    public String doSomethingWithLine(String line, String value) {
                        return value + line;
                    }
                };
        return lineReadTemplate(filePath, concatenateCallback, "");
    }

    public Integer fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            int ret = callback.doSomethingWithReader(br);
            return ret;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try { br.close(); }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            T res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) res = callback.doSomethingWithLine(line, res);
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try { br.close(); }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
