package com.umeng.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.umeng.test.time.ArrayWheelAdapter;
import com.umeng.test.time.OnWheelChangedListener;
import com.umeng.test.time.WheelView;
import com.umeng.test.util.LogUtil;
import com.umeng.test.util.PickerUtil;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private WheelView yearWV = null;
    private WheelView monthWV = null;
    private WheelView dayWV = null;
    private WheelView hourWV = null;

    private PickerUtil pickerUtil;

    int year;
    int month;

    // 滚轮上的数据，字符串数组
    String[] yearArrayString = null;
    String[] monthArrayString = null;
    String[] dayArrayString = null;
    String[] hourArrayString = null;
    Calendar c = null;

    public void btnSingleChoiceActivity(View v) {
        try {
            // startActivity(new Intent(this, SingleChoicActivity.class));
            pickerUtil = new PickerUtil(MainActivity.this);
            String[] arrayString = {"1", "2"};
            pickerUtil.showSingleChooseDialog("选择定金金额", arrayString);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            LogUtil.e(MainActivity.class, "btnSingleChoiceActivity", e);
        }
    }

    public void btnDoubleChoiceActivity(View v) {
        try {
            // startActivity(new Intent(this, SingleChoicActivity.class));
            pickerUtil = new PickerUtil(MainActivity.this);
            String[] arrayString1 = {"上海市", "江苏省", "浙江省"};
            String[][] arrayString2 = {{"黄浦区", "卢湾区", "徐汇区", "长宁区", "静安区", "普陀区", "闸北区", "虹口区", "杨浦区", "闵行区", "宝山区", "浦东新区", "嘉定区"}, {"苏州", "无锡", "启东"}, {"杭州", "宁波", "嘉兴"}};
            pickerUtil.showTwoChooseDialog("选择区域", arrayString1, arrayString2);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            LogUtil.e(MainActivity.class, "btnDoubleChoiceActivity", e);
        }
    }

    public void btnThreeChoiceActivity(View v) {
        try {
            // startActivity(new Intent(this, SingleChoicActivity.class));
            pickerUtil = new PickerUtil(MainActivity.this);
            pickerUtil.showThreeChooseDialog("预计开工时间", false);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            LogUtil.e(MainActivity.class, "btnThreeChoiceActivity", e);
        }
    }

    public void btnFourChoiceActivity(View v) {
        try {
            pickerUtil = new PickerUtil(MainActivity.this);
            pickerUtil.showThreeChooseDialog("预计开工时间", true);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            LogUtil.e(MainActivity.class, "btnFourChoiceActivity", e);
        }
    }

    public void btnFiveChoiceActivity(View v) {
        try {
            String[] array = {"1","2","3","4","5","6","7","8","9","10"};
            pickerUtil = new PickerUtil(MainActivity.this);
            pickerUtil.showFiveChooseDialog("选择房型", array);
            pickerUtil.setCallBack(new PickerUtil.loadDataCallBack() {
                @Override
                public void loadDataSuccess(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            LogUtil.e(PickerUtil.class, "btnFiveChoiceActivity", e);
        }
    }
    public void showTipDialog(View v) {
        try {
            pickerUtil = new PickerUtil(MainActivity.this);
            pickerUtil.showTipDialog(this, "确认保存吗？");
            pickerUtil.setCallBack(new PickerUtil.pressBtCallBack() {
                @Override
                public void pressSuccess(boolean isPressYes) {
                    Toast.makeText(MainActivity.this, isPressYes + "", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            LogUtil.e(MainActivity.class, "showTipDialog", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 得到相应的数组
        yearArrayString = getYEARArray(2010, 19);
        monthArrayString = getDayArray(12);
        hourArrayString = getDayArray(24);

        // 获取当前系统时间
        c = Calendar.getInstance();
        initView();
    }

    private void initView() {
        yearWV = (WheelView) findViewById(R.id.time_year);
        monthWV = (WheelView) findViewById(R.id.time_month);
        dayWV = (WheelView) findViewById(R.id.time_day);
        hourWV = (WheelView) findViewById(R.id.time_hour);

        // 设置每个滚轮的行数
        yearWV.setVisibleItems(5);
        monthWV.setVisibleItems(5);
        dayWV.setVisibleItems(5);
        hourWV.setVisibleItems(5);

        // 设置滚轮的标签
        yearWV.setLabel("年");
        monthWV.setLabel("月");
        dayWV.setLabel("日");
        hourWV.setLabel("时");

        // 设置能否循环滚动
        yearWV.setCyclic(false);
        monthWV.setCyclic(true);
        dayWV.setCyclic(true);
        hourWV.setCyclic(false);
        setData();
    }

    /**
     * 给滚轮提供数据
     */
    private void setData() {
        // 给滚轮提供数据
        yearWV.setAdapter(new ArrayWheelAdapter<String>(yearArrayString));
        monthWV.setAdapter(new ArrayWheelAdapter<String>(monthArrayString));
        hourWV.setAdapter(new ArrayWheelAdapter<String>(hourArrayString));

        yearWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // Auto-generated method stub
                // 获取年和月
                year = Integer.parseInt(yearArrayString[yearWV.getCurrentItem()]);
                month = Integer.parseInt(monthArrayString[monthWV
                        .getCurrentItem()]);
                // 根据年和月生成天数数组
                dayArrayString = getDayArray(getDay(year, month));
                // 给天数的滚轮设置数据
                dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
                // 防止数组越界
                if (dayWV.getCurrentItem() >= dayArrayString.length) {
                    dayWV.setCurrentItem(dayArrayString.length - 1);
                }
                // 显示的时间
                showDate();
            }
        });

        // 当月变化时显示的时间
        monthWV.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // Auto-generated method stub
                // 获取年和月
                year = Integer.parseInt(yearArrayString[yearWV.getCurrentItem()]);
                month = Integer.parseInt(monthArrayString[monthWV
                        .getCurrentItem()]);
                // 根据年和月生成天数数组
                dayArrayString = getDayArray(getDay(year, month));
                // 给天数的滚轮设置数据
                dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
                // 防止数组越界
                if (dayWV.getCurrentItem() >= dayArrayString.length) {
                    dayWV.setCurrentItem(dayArrayString.length - 1);
                }
                // 显示的时间
                showDate();
            }
        });

        // 当天变化时，显示的时间
        dayWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                showDate();
            }
        });

        // 当小时变化时，显示的时间
        hourWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                showDate();
            }
        });

        // 把当前系统时间显示为滚轮默认时间
        setOriTime();
    }

    // 设定初始时间
    void setOriTime() {
        yearWV.setCurrentItem(getNumData(c.get(Calendar.YEAR) + "",
                yearArrayString));
        monthWV.setCurrentItem(getNumData(c.get(Calendar.MONTH) + 1 + "",
                monthArrayString) + 0);
        hourWV.setCurrentItem(getNumData(c.get(Calendar.HOUR_OF_DAY) + "",
                hourArrayString));

        dayArrayString = getDayArray(getDay(year, month));
        dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
        dayWV.setCurrentItem(getNumData(c.get(Calendar.DAY_OF_MONTH) + "",
                dayArrayString));

        // 初始化显示的时间
        showDate();
    }

    // 显示时间
    void showDate() {
        createDate(yearArrayString[yearWV.getCurrentItem()],
                monthArrayString[monthWV.getCurrentItem()],
                dayArrayString[dayWV.getCurrentItem()],
                hourArrayString[hourWV.getCurrentItem()]);
    }

    // 生成时间
    void createDate(String year, String month, String day, String hour) {
        String dateStr = year + "年" + month + "月" + day + "日" + hour + "时";
//		Toast.makeText(this, "选择时间为：" + dateStr, Toast.LENGTH_LONG).show();
    }

    // 在数组Array[]中找出字符串s的位置
    int getNumData(String s, String[] Array) {
        int num = 0;
        for (int i = 0; i < Array.length; i++) {
            if (s.equals(Array[i])) {
                num = i;
                break;
            }
        }
        return num;
    }

    // 根据当前年份和月份判断这个月的天数
    public int getDay(int year, int month) {
        int day;
        if (year % 4 == 0 && year % 100 != 0) { // 闰年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 29;
            } else {
                day = 30;
            }
        } else { // 平年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 28;
            } else {
                day = 30;
            }
        }
        return day;
    }

    // 根据数字生成一个字符串数组
    public String[] getDayArray(int day) {
        String[] dayArr = new String[day];
        for (int i = 0; i < day; i++) {
            dayArr[i] = i + 1 + "";
        }
        return dayArr;
    }

    // 根据数字生成一个字符串数组
    public String[] getHMArray(int day) {
        String[] dayArr = {"上午", "下午"};
        return dayArr;
    }

    // 根据初始值start和step得到一个字符数组，自start起至start+step-1
    public String[] getYEARArray(int start, int step) {
        String[] dayArr = new String[step];
        for (int i = 0; i < step; i++) {
            dayArr[i] = start + i + "";
        }
        return dayArr;
    }
}
