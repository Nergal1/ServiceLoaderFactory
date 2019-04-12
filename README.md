# ServiceLoaderFactory

#### 项目介绍
基于Google AutoService SPI的轻量化Android快速开发框架

#### 具体参见[博客](https://www.jianshu.com/p/7c6ba4fd6f09)

一、什么是SPI
SPI : Service Provider Interfaces即Service提供者接口，正如从SPI的名字去理解SPI就是Service提供者接口；SPI的定义：提供给服务提供厂商与扩展框架功能的开发者使用的接口。

讲到SPI就会涉及到 ServiceLoader 类 。简单的理解为一个简单的服务提供者加载设施。服务 是一个熟知的接口和类（通常为抽象类）集合。服务提供者 是服务的特定实现。提供者中的类通常实现接口，并子类化在服务本身中定义的子类。服务提供者可以以扩展的形式安装在 Java 平台的实现中，也就是将 jar 文件放入任意常用的扩展目录中。也可通过将提供者加入应用程序类路径，或者通过其他某些特定于平台的方式使其可用。

具体请看 ServiceLoader详解 。

二、AutoService
上面是在java上的SPI实现，在Andorid工程中build下并没有META-INF ，也就无法进行Loader，这里需要用到Google的开源 AutoService 框架。

三、基于AutoService 的Android快速开发框架
效果图：

![image](https://github.com/Nergal1/ServiceLoaderFactory/blob/master/3de814524cc3755866334ddf0f6c100f.png)

image

项目结构图：

![image](https://github.com/Nergal1/ServiceLoaderFactory/blob/master/6f07ae406fd8895e7856778e7b78542e.png)

image

说明：

common-library: ServiceLoader 处理的工具类和Tab配置注解。
main-module: 主框架。
first-module:第一个module。
second-module： 第二个module。
1、ServiceLoader 处理的工具类
Services.java

public final class Services {

    public static  T load(Class tClass) throws ServiceNotFoundException {
        Iterator iterator = ServiceLoader.load(tClass)
                .iterator();
        if (iterator.hasNext()) {
            return iterator
                    .next();
        } else {
            throw new ServiceNotFoundException();
        }
    }
}
通过Iterator来获取所有tClass 类型的接口集合。

2、定义一个Annotation Tab配置注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ITabPage {

    // tab标题
    String tabName();
    // tab 图标
    String iconName();
}
这个注解主要作用是配置主框架底部Tab的标题和图标。

在定义一个接口用来实现主框架跳转页面。IStartMainContainer.java

public interface IStartMainContainer {

    void startMainActivity(Context context);
}
3、主框架MainContainerActivity
public class MainContainerActivity extends AppCompatActivity {

    private BottomNavigationView mNavigation;

    private List mFragmentList = new ArrayList();

    public static void start(Context context) {
        Intent intent = new Intent(context,MainContainerActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container_activity);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        serviceLoad();
        initEvent();

        mNavigation.setSelectedItemId(mNavigation.getMenu().getItem(0).getItemId());
    }

    private void serviceLoad(){
        //加载Fragment 类型的ServiceLoader
        ServiceLoader iterator = ServiceLoader.load(Fragment.class);

        for (Iterator iterator1 = iterator.iterator(); iterator1.hasNext(); ) {
            final Fragment fragment = iterator1.next();

            //获取ITabPage 注解的类
            ITabPage property = fragment.getClass().getAnnotation(ITabPage.class);
            if (property == null) {
                continue;
            }
            Menu menu = mNavigation.getMenu();
            MenuItem item = menu.add(property.tabName());
            int drawable = getResources().getIdentifier(property.iconName(), "drawable", getPackageName());
            item.setIcon(drawable);
            mFragmentList.add(fragment);
        }

    }

    private void initEvent(){
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int size = mNavigation.getMenu().size();
                int index = 0;

                for (int i = 0; i < size; i++) {
                    if (mNavigation.getMenu().getItem(i) == item) {
                        index = i;
                        break;
                    }
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, mFragmentList.get(index))
                        .commit();

                return true;
            }
        });
    }

}
重点是serviceLoad()方法里面：

首先通过ServiceLoader将有通过AutoService标记的Fragment全部读取出来。
然后在通过Annotation读取ITabPage的Tab配置。
BottomNavigationView底部导航。
在主框架里面定义一个类通过标记AutoService为 IStartMainContainer.class 来给外部启动MainContainerActivity。

@AutoService(IStartMainContainer.class)
public class StartMainContainer implements IStartMainContainer {
    @Override
    public void startMainActivity(Context context) {
        //启动MainContainerActivity
        MainContainerActivity.start(context);
    }
}
4、主框架Tab的Fragment具体实现
在first-module 里面有一个Fragment：

@ITabPage(tabName = "用户", iconName = "tab_user")
@AutoService(Fragment.class)
public class FirstFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_fragment, container, false);
    }

}
在second-module里面也有一个Fragment：

@ITabPage(tabName = "工作", iconName = "tab_work")
@AutoService(Fragment.class)
public class SecondFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_fragment, container, false);
    }

}
这样将这两个Fragment通过AutoService标记为Fragment.class，然后在主框架MainContainerActivity中读取。并且通过@ITabPage 的注解进行Tab属性的配置。

跳转主框架
在app的启动Activity的MainActivity中跳转到主框架MainContainerActivity。

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 通过Service.load 来加载已经标识IStartMainContainer.class 的注解的类，在main-module里面的StartMainContainer类
        Services.load(IStartMainContainer.class)
                .startMainActivity(this);

        finish();
    }
}
至此，一个快速开发框架搭建好了。
