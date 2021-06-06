package com.example.czsc.common.enums;

/**
 * @FileName: IndustryEnum
 * @Author: Haifeng Tong
 * @Date: 2021/3/279:45 下午
 * @Description:
 * @History:
 * 申万行业1级
 */
public enum IndustryEnum {
    //一级行业，28个
    Excavation("210000","采掘", LevelEnum.FIRST,null,"801020"),
    Chemical_Industry("220000","化工", LevelEnum.FIRST,null,"801030"),
    Steel("230000","钢铁", LevelEnum.FIRST,null," 801040"),
    Nonferrous_Metals("240000","有色金属", LevelEnum.FIRST,null,"801050"),
    Building_Material("610000","建筑材料", LevelEnum.FIRST,null,"801710"),
    Architectural_Decoration("620000","建筑装饰", LevelEnum.FIRST,null,"801720"),
    Electrical_Equipment("630000","电气设备", LevelEnum.FIRST,null,"801730"),
    Mechanical_Equipment("640000","机械设备", LevelEnum.FIRST,null,"801890"),
    National_Defense_Industry("650000","国防军工", LevelEnum.FIRST,null,"801740"),
    Automobile("280000","汽车", LevelEnum.FIRST,null,"801880"),
    Household_Appliances("330000","家用电器", LevelEnum.FIRST,null,"801110"),
    Light_Manufacturing("360000","轻工制造", LevelEnum.FIRST,null,"801140"),
    Agriculture("110000","农林牧渔", LevelEnum.FIRST,null,"801010"),
    Food_Beverage("340000","食品饮料", LevelEnum.FIRST,null,"801120"),
    Textile_Clothing("350000","纺织服装", LevelEnum.FIRST,null,"801130"),
    Medical_Biology("370000","医药生物", LevelEnum.FIRST,null,"801150"),
    Commercial_Trade("450000","商业贸易", LevelEnum.FIRST,null,"801200"),
    Leisure_Services("460000","休闲服务", LevelEnum.FIRST,null,"801210"),
    Electronics("270000","电子", LevelEnum.FIRST,null,"801080"),
    Computer("710000","计算机", LevelEnum.FIRST,null,"801750"),
    Media("720000","传媒", LevelEnum.FIRST,null,"801760"),
    Communication("730000","通信", LevelEnum.FIRST,null,"801770"),
    Public_Utilities("410000","公用事业", LevelEnum.FIRST,null," 801160"),
    Transportation("420000","交通运输", LevelEnum.FIRST,null,"801170"),
    Real_Estate("430000","房地产", LevelEnum.FIRST,null,"801180"),
    Bank("480000","银行", LevelEnum.FIRST,null,"801780"),
    Non_Bank_Finance("490000","非银金融", LevelEnum.FIRST,null,"801790"),
    Comprehensive("510000","综合", LevelEnum.FIRST,null,"801230"),
    //二级行业
    Oil_Exploitation("210100","石油开采", LevelEnum.SECOND,Excavation,"801023"),
    Coal_Mining("210200","煤炭开采", LevelEnum.SECOND,Excavation,"801021"),
    Other_Mining("210300","其他采掘", LevelEnum.SECOND,Excavation,"801022"),
    Mining_Service("210400","采掘服务", LevelEnum.SECOND,Excavation,"801024"),

    Petrochemical_Industry("220100","石油化工", LevelEnum.SECOND,Chemical_Industry,"801035"),
    Chemical_Materials("220200","化学原料", LevelEnum.SECOND,Chemical_Industry,"801033"),
    Chemicals("220300","化学制品", LevelEnum.SECOND,Chemical_Industry,"801034"),
    Chemical_Fiber("220400","化学纤维", LevelEnum.SECOND,Chemical_Industry,"801032"),
    Plastic("220500","塑料", LevelEnum.SECOND,Chemical_Industry,"801036"),
    Rubber("220600","橡胶", LevelEnum.SECOND,Chemical_Industry,"801037"),

    Steel_Secondary("230100","钢铁", LevelEnum.SECOND,Steel,"801041"),

    Industrial_Metals("240300","工业金属", LevelEnum.SECOND,Nonferrous_Metals,"801055"),
    Gold("240400","黄金", LevelEnum.SECOND,Nonferrous_Metals,"801053"),
    Rare_Metals("240500","稀有金属", LevelEnum.SECOND,Nonferrous_Metals,"801054"),
    New_Metal_Nonmetal_Materials("240200","金属非金属新材料", LevelEnum.SECOND,Nonferrous_Metals,"801051"),

    Cement_Manufacturing("610100","水泥制造", LevelEnum.SECOND,Building_Material,"801711"),
    Glass_making("610200","玻璃制造", LevelEnum.SECOND,Building_Material,"801712"),
    Other_Building_Materials("610300","其他建材", LevelEnum.SECOND,Building_Material,"801713"),

    Housing_Construction("620100","房屋建设", LevelEnum.SECOND,Architectural_Decoration,"801721"),
    Decoration("620200","装修装饰", LevelEnum.SECOND,Architectural_Decoration,"801722"),
    Infrastructure("620300","基础建设", LevelEnum.SECOND,Architectural_Decoration,"801723"),
    Professional_Engineering("620400","专业工程", LevelEnum.SECOND,Architectural_Decoration,"801724"),
    LandscapeEngineering("620500","园林工程", LevelEnum.SECOND,Architectural_Decoration,"801725"),

    Electric_Machinery("630100","电机", LevelEnum.SECOND,Electrical_Equipment,"801731"),
    Electrical_Automation_Equipment("630200","电气自动化设备", LevelEnum.SECOND,Electrical_Equipment,"801732"),
    Power_Equipment("630300","电源设备", LevelEnum.SECOND,Electrical_Equipment,"801733"),
    High_Low_Voltage_Equipment("630400","高低压设备", LevelEnum.SECOND,Electrical_Equipment,"801734"),

    General_Machinery("640100","通用机械", LevelEnum.SECOND,Mechanical_Equipment,"801072"),
    Special_Equipment("640200","专用设备", LevelEnum.SECOND,Mechanical_Equipment,"801074"),
    Instrumentation("640300","仪器仪表", LevelEnum.SECOND,Mechanical_Equipment,"801073"),
    Metalware("640400","金属制品", LevelEnum.SECOND,Mechanical_Equipment,"801075"),
    Transport_Equipment("640500","运输设备", LevelEnum.SECOND,Mechanical_Equipment,"801076"),

    Space_Equipment("650100","航天装备", LevelEnum.SECOND,National_Defense_Industry,"801741"),
    Aviation_Equipment("650200","航空装备", LevelEnum.SECOND,National_Defense_Industry,"801742"),
    Ground_Uniform("650300","地面兵装", LevelEnum.SECOND,National_Defense_Industry,"801743"),
    Shipbuilding("650400","船舶制造", LevelEnum.SECOND,National_Defense_Industry,"801744"),

    Entirety_Vehicle("280100","汽车整车", LevelEnum.SECOND,Automobile,"801094"),
    Vehicle_Parts("280200","汽车零部件", LevelEnum.SECOND,Automobile,"801093"),
    Vehicle__Service("280300","汽车服务", LevelEnum.SECOND,Automobile,"801092"),
    Other_Deliver_Equipment("280400","其他交运设备", LevelEnum.SECOND,Automobile,"801881"),

    White_Goods("330100","白色家电", LevelEnum.SECOND,Household_Appliances,"801111"),
    Audio_Visual_Equipment("330200","视听器材", LevelEnum.SECOND,Household_Appliances,"801112"),

    Papermaking("360100","造纸", LevelEnum.SECOND,Light_Manufacturing,"801143"),
    Packaging_Printing("360200","包装印刷", LevelEnum.SECOND,Light_Manufacturing,"801141"),
    Household_Light_Industry("360300","家用轻工", LevelEnum.SECOND,Light_Manufacturing,"801142"),
    Other_Light_Industry("360400","其他轻工制造", LevelEnum.SECOND,Light_Manufacturing,"801144"),

    Planting("110100","种植业", LevelEnum.SECOND,Agriculture,"801016"),
    Fishery("110200","渔业", LevelEnum.SECOND,Agriculture,"801015"),
    Forestry("110300","林业", LevelEnum.SECOND,Agriculture,"801011"),
    Feed("110400","饲料", LevelEnum.SECOND,Agriculture,"801014"),
    Agrotechny("110500","农产品加工", LevelEnum.SECOND,Agriculture,"801012"),
    Agribusiness("110600","农业综合", LevelEnum.SECOND,Agriculture,"801013"),
    Livestock("110700","畜禽养殖", LevelEnum.SECOND,Agriculture,"801017"),
    Animal_Health("110800","动物保健", LevelEnum.SECOND,Agriculture,"801018"),

    Drink_Manufacturing("340300","饮料制造", LevelEnum.SECOND,Food_Beverage,"801123"),
    Food_Processing("340400","食品加工", LevelEnum.SECOND,Food_Beverage,"801124"),

    Textile_Manufacturing("350100","纺织制造", LevelEnum.SECOND,Textile_Clothing,"801131"),
    Clothing("350200","服装家纺", LevelEnum.SECOND,Textile_Clothing,"801132"),

    Chemical_Pharmacy("370100","化学制药", LevelEnum.SECOND,Medical_Biology,"801151"),
    Chinese_Medicine("370200","中药", LevelEnum.SECOND,Medical_Biology,"801155"),
    Biological_Products("370300","生物制品", LevelEnum.SECOND,Medical_Biology,"801152"),
    Medicine_Business("370400","医药商业", LevelEnum.SECOND,Medical_Biology,"801154"),
    Medical_Instrument("370500","医疗器械", LevelEnum.SECOND,Medical_Biology,"801153"),
    Medical_Service("370600","医疗服务", LevelEnum.SECOND,Medical_Biology,"801156"),

    Retail("450300","一般零售", LevelEnum.SECOND,Commercial_Trade,"801203"),
    Profession_retail("450400","专业零售", LevelEnum.SECOND,Commercial_Trade,"801204"),
    Property_Management("450500","商业物业经营", LevelEnum.SECOND,Commercial_Trade,"801205"),
    Trade("450200","贸易", LevelEnum.SECOND,Commercial_Trade,"801202"),

    Scenic_Spot("460100","景点", LevelEnum.SECOND,Leisure_Services,"801212"),
    Hotel("460200","酒店", LevelEnum.SECOND,Leisure_Services,"801213"),
    Tourism_Integration("460300","旅游综合", LevelEnum.SECOND,Leisure_Services,"801214"),
    Restaurant("460400","餐饮", LevelEnum.SECOND,Leisure_Services,"801211"),
    Other_Leisure_Service("460500","其他休闲服务", LevelEnum.SECOND,Leisure_Services,"801215"),

    Semiconductor("270100","半导体", LevelEnum.SECOND,Electronics,"801081"),
    Circuit_Component("270200","元件", LevelEnum.SECOND,Electronics,"801083"),
    Photoelectric("270300","光学光电子", LevelEnum.SECOND,Electronics,"801084"),
    Electronic_Manufacturing("270500","电子制造", LevelEnum.SECOND,Electronics,"801085"),
    Other_Electronics("270400","其他电子", LevelEnum.SECOND,Electronics,"801082"),

    Computer_Equipment("710100","计算机设备", LevelEnum.SECOND,Computer,"801101"),
    Computer_Application("710200","计算机应用", LevelEnum.SECOND,Computer,"801222"),

    Cultural_Media("720100","文化传媒", LevelEnum.SECOND,Media,"801761"),
    Marketing_Communication("720200","营销传播", LevelEnum.SECOND,Media,"801751"),
    Internet_Media("720300","互联网传媒", LevelEnum.SECOND,Media,"801752"),

    Communication_Operation("730100","通信运营", LevelEnum.SECOND,Communication,"801223"),
    Communication_Equipment("730200","通信设备", LevelEnum.SECOND,Communication,"801102"),

    Power("410100","电力", LevelEnum.SECOND,Public_Utilities,"801161"),
    Water_Affairs("410200","水务", LevelEnum.SECOND,Public_Utilities,"801164"),
    Gas("410300","燃气", LevelEnum.SECOND,Public_Utilities,"801163"),
    Environmantal_Engineering_Service("410400","环保工程及服务", LevelEnum.SECOND,Public_Utilities,"801162"),

    Port("420100","港口", LevelEnum.SECOND,Transportation,"801171"),
    Expressway("420200","高速公路", LevelEnum.SECOND,Transportation,"801175"),
    Transit("420300","公交", LevelEnum.SECOND,Transportation,"801172"),
    Air_Transport("420400","航空运输", LevelEnum.SECOND,Transportation,"801173"),
    Airport("420500","机场", LevelEnum.SECOND,Transportation,"801174"),
    Shipping("420600","航运", LevelEnum.SECOND,Transportation,"801176"),
    Railway_Transportation("420700","铁路运输", LevelEnum.SECOND,Transportation,"801177"),
    Logistics("420800","物流", LevelEnum.SECOND,Transportation,"801178"),

    Real_Estate_Development("430100","房地产开发", LevelEnum.SECOND,Real_Estate,"801181"),
    Park_Development("430200","园区开发", LevelEnum.SECOND,Real_Estate,"801182"),

    Bank_Secondary("480000","银行", LevelEnum.SECOND,Bank,"801192"),

    Security_Bound("490100","证券", LevelEnum.SECOND,Non_Bank_Finance,"801193"),
    Insurance("490200","保险", LevelEnum.SECOND,Non_Bank_Finance,"801194"),
    Diversified_Finance("490300","多元金融", LevelEnum.SECOND,Non_Bank_Finance,"801191"),
    Muptiple("510000","综合", LevelEnum.SECOND,Comprehensive,"801231"),

    //三级行业
    Oil_Exp_level3("210101","石油开采", LevelEnum.THIRD,Oil_Exploitation,"850211"),

    Coal_Mining_Level3("210201","煤炭开采", LevelEnum.SECOND,Coal_Mining,"850221"),
    Coking_Processing_Level3("210202","焦炭加工", LevelEnum.SECOND,Coal_Mining,"850222"),

    Other_Mining_Level3("210301","其他采掘", LevelEnum.SECOND,Other_Mining,"850231"),

    Oil_Gas_Drilling("210401","油气钻采服务", LevelEnum.SECOND,Mining_Service,"850241"),
    Other_Mining_Services("210402","其他采掘服务", LevelEnum.SECOND,Mining_Service,"850242"),

    Petro_Industry_Level3("220101","石油加工", LevelEnum.THIRD,Petrochemical_Industry,"850311"),
    Petro_Industry_Business("220103","石油贸易", LevelEnum.THIRD,Petrochemical_Industry,"850313"),

    Soda_Ash("220201","纯碱", LevelEnum.THIRD,Chemical_Materials,"850321"),
    Chlor_Alkali("220202","氯碱", LevelEnum.THIRD,Chemical_Materials,"850322"),
    Inorganic_Salt("220203","无机盐", LevelEnum.THIRD,Chemical_Materials,"850323"),
    Other_Chemical_Materials("220204","其他化学原料", LevelEnum.THIRD,Chemical_Materials,"850324"),

    Nitrogen_Fertilizer("220301","氮肥", LevelEnum.THIRD,Chemicals,"850331"),
    Photosphate_Fertilizer("220302","磷肥", LevelEnum.THIRD,Chemicals,"850332"),
    Potash_Fertilizer("220306","钾肥", LevelEnum.THIRD,Chemicals,"850336"),
    Compound_Fertilizer("220310","复合肥", LevelEnum.THIRD,Chemicals,"850381"),
    Pesticides("220303","农药", LevelEnum.THIRD,Chemicals,"850333"),
    Daily_Chemical("220304","日用化学产品", LevelEnum.THIRD,Chemicals,"850334"),
    Pink_Ink_Manufacturing("220305","涂料油漆油墨制造", LevelEnum.THIRD,Chemicals,"850335"),
    Civil_Explosive_Products("220307","民爆用品", LevelEnum.THIRD,Chemicals,"850337"),
    Textile_Chemicals("220308","纺织化学用品", LevelEnum.THIRD,Chemicals,"850338"),
    Refrigerant_Cooling("220311","氟化工及制冷剂", LevelEnum.THIRD,Chemicals,"850382"),
    Phososphate("220312","磷化工及磷酸盐", LevelEnum.THIRD,Chemicals,"850383"),
    Polyurethane("220313","聚氨酯", LevelEnum.THIRD,Chemicals,"850372"),
    Glass_Fiber("220314","玻纤", LevelEnum.THIRD,Chemicals,"850373"),
    Other_Chemicals("220309","其他化学制品", LevelEnum.THIRD,Chemicals,"850339"),


    Polyester_Fiber("220401","涤纶", LevelEnum.THIRD,Chemical_Fiber,"850341"),
    Vinylon("220402","维纶", LevelEnum.THIRD,Chemical_Fiber,"850342"),
    Viscose("220403","粘胶", LevelEnum.THIRD,Chemical_Fiber,"850343"),
    Spandex("220405","氨纶", LevelEnum.THIRD,Chemical_Fiber,"850345"),
    Other_Fibers("220404","其他纤维", LevelEnum.THIRD,Chemical_Fiber,"850344"),

    Synthetic_Leather("220502","合成革", LevelEnum.THIRD,Plastic,"850352"),
    Modified_Plastics("220503","改性塑料", LevelEnum.THIRD,Plastic,"850353"),
    Other_Plastic("220501","其他塑料制品", LevelEnum.THIRD,Plastic,"850351"),

    Tyre("220601","轮胎", LevelEnum.THIRD,Rubber,"850361"),
    Other_Rubber_Products("220602","其他橡胶制品", LevelEnum.THIRD,Rubber,"850362"),
    Carbon_Black("220603","炭黑", LevelEnum.THIRD,Rubber,"850363"),

    General_Steel("230101","普钢", LevelEnum.THIRD,Steel_Secondary,"850411"),

    Aluminium("240301","铝", LevelEnum.THIRD,Industrial_Metals,"850551"),
    Copper("240302","铜", LevelEnum.THIRD,Industrial_Metals,"850552"),
    Lead_Zinc("240303","铅锌", LevelEnum.THIRD,Industrial_Metals,"850553"),

    Gold_Level3("240401","黄金", LevelEnum.THIRD,Gold,"850531"),

    Rare_Earth("240501","稀土", LevelEnum.THIRD,Rare_Metals,"850541"),
    Tungsten("240502","钨", LevelEnum.THIRD,Rare_Metals,"850542"),
    Lithium("240503","锂", LevelEnum.THIRD,Rare_Metals,"850543"),
    Other_Rare_Matals("240504","其他稀有小金属", LevelEnum.THIRD,Rare_Metals,"850544"),

    New_Metal_Materials("240201","金属新材料", LevelEnum.THIRD,New_Metal_Nonmetal_Materials,"850521"),
    Magnetic_Material("240202","磁性材料", LevelEnum.THIRD,New_Metal_Nonmetal_Materials,"850522"),
    Nonmetal_Materials("240203","非金属新材料", LevelEnum.THIRD,New_Metal_Nonmetal_Materials,"850523"),

    Cement_Manu_Level3("610101","水泥制造", LevelEnum.THIRD,Cement_Manufacturing,"850612"),

    Glass_making_Level3("610201","玻璃制造", LevelEnum.THIRD,Glass_making,"850611"),

    Refractories("610301","耐火材料", LevelEnum.THIRD,Other_Building_Materials,"850615"),
    Pipe("610302","管材", LevelEnum.THIRD,Other_Building_Materials,"850616"),
    Other_Build_Materials("610303","其他建材", LevelEnum.THIRD,Other_Building_Materials,"850614"),

    Housing_Construct_Level3("620101","房屋建设", LevelEnum.THIRD,Housing_Construction,"850623"),

    Decorate_Level3("620201","装修装饰", LevelEnum.THIRD, Decoration,"857221"),

    Urban_Rail_Transit("620301","城轨建设", LevelEnum.THIRD,Infrastructure,"857231"),
    Road_Bridge_Construction("620302","路桥施工", LevelEnum.THIRD,Infrastructure,"857232"),
    Irrigation_Works("620303","水利工程", LevelEnum.THIRD,Infrastructure,"857233"),
    Railway_Construction("620304","铁路建设", LevelEnum.THIRD,Infrastructure,"857234"),
    Other_Infrastructure("620305","其他基础建设", LevelEnum.THIRD,Infrastructure,"857235"),

    Steel_Structure("620401","钢结构", LevelEnum.THIRD,Professional_Engineering,"857241"),
    Chemical_Engineering("620402","化学工程", LevelEnum.THIRD,Professional_Engineering,"857242"),
    International_Engineering("620403","国际工程承包", LevelEnum.THIRD,Professional_Engineering,"857243"),
    Other_Professional_Project("620404","其他专业工程", LevelEnum.THIRD,Professional_Engineering,"857244"),

    Scene_Project("620501","园林工程", LevelEnum.THIRD,LandscapeEngineering,"857251"),

    Electric_Machine_Level3("630101","电机", LevelEnum.THIRD,Electric_Machinery,"850741"),

    Power_Grid_Automation("630201","电网自动化", LevelEnum.THIRD,Electrical_Automation_Equipment,"857321"),
    Industrial_Control("630202","工控自动化", LevelEnum.THIRD,Electrical_Automation_Equipment,"857322"),
    Measuring_Instrument("630203","计量仪表", LevelEnum.THIRD,Electrical_Automation_Equipment,"857323"),

    Integrated_Power_Equip("630301","综合电力设备商", LevelEnum.THIRD,Power_Equipment,"857331"),
    Wind_Power_Equip("630302","风电设备", LevelEnum.THIRD,Power_Equipment,"857332"),
    Photovoltaic_Equip("630303","光伏设备", LevelEnum.THIRD,Power_Equipment,"857333"),
    Thermal_Electricity("630304","火电设备", LevelEnum.THIRD,Power_Equipment,"857334"),
    Energy_Storage_Equip("630305","储能设备", LevelEnum.THIRD,Power_Equipment,"857335"),
    Other_Power_Supply("630306","其它电源设备", LevelEnum.THIRD,Power_Equipment,"857336"),

    High_Voltage_Equip("630401","高压设备", LevelEnum.THIRD,High_Low_Voltage_Equipment,"857341"),
    Medium_Voltage_Equip("630402","中压设备", LevelEnum.THIRD,High_Low_Voltage_Equipment,"857342"),
    Low_Voltage_Equip("630403","低压设备", LevelEnum.THIRD,High_Low_Voltage_Equipment,"857343"),
    Cable_Compo_Other("630404","线缆部件及其他", LevelEnum.THIRD,High_Low_Voltage_Equipment,"857344"),

    Machine_Tools("640101","机床工具", LevelEnum.THIRD,General_Machinery,"850711"),
    Mechanical_Basis("640102","机械基础件", LevelEnum.THIRD,General_Machinery,"850712"),
    Abrasives("640103","磨具磨料", LevelEnum.THIRD,General_Machinery,"850713"),
    Explorison_Engine("640104","内燃机", LevelEnum.THIRD,General_Machinery,"850714"),
    Air_Conditioning_Equip("640105","制冷空调设备", LevelEnum.THIRD,General_Machinery,"850715"),
    Other_General_Mechinery("640106","其它通用机械", LevelEnum.THIRD,General_Machinery,"850716"),

    Construct_Machinery("640201","工程机械", LevelEnum.THIRD,Special_Equipment,"850722"),
    Heavy_Machinery("640202","重型机械", LevelEnum.THIRD,Special_Equipment,"850724"),
    Metallurgicall_Mining_Chemical_Equip("640203","冶金矿采化工设备", LevelEnum.THIRD,Special_Equipment,"850725"),
    Building_Equip("640204","楼宇设备", LevelEnum.THIRD,Special_Equipment,"850728"),
    Encironment_Equip("640205","环保设备", LevelEnum.THIRD,Special_Equipment,"850729"),
    Textile_Garment_Equip("640206","纺织服装设备", LevelEnum.THIRD,Special_Equipment,"850721"),
    Agricultural_Machinery("640207","农用机械", LevelEnum.THIRD,Special_Equipment,"850723"),
    Printing_Packing_Machinery("640208","印刷包装机械", LevelEnum.THIRD,Special_Equipment,"850726"),
    Other_Special_Machinery("640209","其它专用机械", LevelEnum.THIRD,Special_Equipment,"850727"),

    Instrument_Level3("640301","仪器仪表", LevelEnum.THIRD, Metalware,"850731"),
    Metal_Level3("640401","金属制品", LevelEnum.THIRD,Metalware,"850751"),
    Railway_Equipment("640501","铁路设备", LevelEnum.THIRD,Transport_Equipment,"850936"),

    Space_Equip_Level3("650101","航天装备", LevelEnum.THIRD,Space_Equipment,"857411"),
    Aviation_Equip_Level3("650201","航空装备", LevelEnum.THIRD,Aviation_Equipment,"857421"),
    Ground_Uniform_Level3("650301","地面兵装", LevelEnum.THIRD,Ground_Uniform,"857431"),
    Shipbuilding_Level3("650401","船舶制造", LevelEnum.THIRD,Shipbuilding,"850935"),

    Car("280101","乘用车", LevelEnum.THIRD,Entirety_Vehicle,"850911"),
    Carry_Cargo("280102","商用载货车", LevelEnum.THIRD,Entirety_Vehicle,"850912"),
    Bus("280103","商用载客车", LevelEnum.THIRD,Entirety_Vehicle,"850913"),

    Vehicle_Parts_Level3("280201","汽车零部件", LevelEnum.THIRD,Vehicle_Parts,"850921"),
    Vehicle__Service_Level3("280301","汽车服务", LevelEnum.THIRD,Vehicle__Service,"850941"),
    Other_Deliver_Equip_Level3("280401","其他交运设备", LevelEnum.THIRD,Other_Deliver_Equipment,"858811"),

    Refrigerator("330101","冰箱", LevelEnum.THIRD,White_Goods,"851111"),
    Air_Conditioner("330102","空调", LevelEnum.THIRD,White_Goods,"851112"),
    Washing_Machine("330103","洗衣机", LevelEnum.THIRD,White_Goods,"851113"),
    Small_Home_Appliances("330104","小家电", LevelEnum.THIRD,White_Goods,"851114"),
    Home_Appliance_Parts("330105","家电零部件", LevelEnum.THIRD,White_Goods,"851115"),

    Color_TV("330201","彩电", LevelEnum.THIRD,Audio_Visual_Equipment,"851121"),
    Other_Audio_Visual_Equip("330202","其它视听器材", LevelEnum.THIRD,Audio_Visual_Equipment,"851122"),

    Papermake_Level3("360101","造纸", LevelEnum.THIRD,Papermaking,"851411"),
    Package_Print_Level3("360201","包装印刷", LevelEnum.THIRD,Packaging_Printing,"851421"),

    Furniture("360302","家具", LevelEnum.THIRD,Household_Light_Industry,"851432"),
    Other_House_Light_Industry("360303","其他家用轻工", LevelEnum.THIRD,Household_Light_Industry,"851433"),
    Jewelry("360304","珠宝首饰", LevelEnum.THIRD,Household_Light_Industry,"851434"),
    Entertainment_Products("360305","文娱用品", LevelEnum.THIRD,Household_Light_Industry,"851435"),

    Other_Light_Industry_level3("360401","其他轻工制造", LevelEnum.THIRD,Other_Light_Industry,"851441"),

    Seed_Breeding("110101","种子生产", LevelEnum.THIRD,Planting,"850111"),
    Grain_Planting("110102","粮食种植", LevelEnum.THIRD,Planting,"850112"),
    Ohter_Planting_Industries("110103","其他种植业", LevelEnum.THIRD,Planting,"850113"),

    Marine_Fishing("110201","海洋捕捞", LevelEnum.THIRD,Fishery,"850121"),
    Aquaculture("110202","水产养殖", LevelEnum.THIRD,Fishery,"850122"),

    Forestry_Level3("110301","林业", LevelEnum.THIRD,Forestry,"801011"),
    Feed_Level3("110401","饲料", LevelEnum.THIRD,Feed,"850141"),
    Fruit_Vegetable_Processing("110501","果蔬加工", LevelEnum.THIRD,Agrotechny,"850151"),
    Grain_Oil_Processing("110502","粮油加工", LevelEnum.THIRD,Agrotechny,"850152"),
    Other_Agricultural("110504","其他农产品加工", LevelEnum.THIRD,Agrotechny,"850154"),

    Agri_Level3("110601","农业综合", LevelEnum.THIRD,Agribusiness,"850161"),
    Livestock_Level3("110701","畜禽养殖", LevelEnum.THIRD,Livestock,"850171"),
    Animal_Health_Level3("110801","动物保健", LevelEnum.THIRD,Animal_Health,"850181"),

    Baijiu("340301","白酒", LevelEnum.THIRD,Drink_Manufacturing,"851231"),
    Beer("340302","啤酒", LevelEnum.THIRD,Drink_Manufacturing,"851232"),
    Other_Alcoholic("340303","其他酒类", LevelEnum.THIRD,Drink_Manufacturing,"851233"),
    Soft_Drink("340304","软饮料", LevelEnum.THIRD,Drink_Manufacturing,"851234"),
    Wine("340305","葡萄酒", LevelEnum.THIRD,Drink_Manufacturing,"851235"),
    Yellow_Wine("340306 ","黄酒", LevelEnum.THIRD,Drink_Manufacturing,"851236"),

    Meat_Product("340401","肉制品", LevelEnum.THIRD,Food_Processing,"851241"),
    Fermented_Flavored_Products("340402","调味发酵品", LevelEnum.THIRD,Food_Processing,"851242"),
    Dairy("340403","乳品", LevelEnum.THIRD,Food_Processing,"851243"),
    Synthesis_Food("340404","食品综合", LevelEnum.THIRD,Food_Processing,"851244"),

    Woolen_Fabric("350101","毛纺", LevelEnum.THIRD,Textile_Manufacturing,"851311"),
    Cotton_Textiles("350102","棉纺", LevelEnum.THIRD,Textile_Manufacturing,"851312"),
    Silk("350103","丝绸", LevelEnum.THIRD,Textile_Manufacturing,"851313"),
    Print_Dyeing("350104","印染", LevelEnum.THIRD,Textile_Manufacturing,"851314"),
    Textile_Accessories("350105","辅料", LevelEnum.THIRD,Textile_Manufacturing,"851315"),
    Other_Textiles("350106","其他纺织", LevelEnum.THIRD,Textile_Manufacturing,"851316"),

    Mens_Wear("350202","男装", LevelEnum.THIRD,Clothing,"851322"),
    Womens_Ware("350203","女装", LevelEnum.THIRD,Clothing,"851323"),
    Casual_Ware("350204","休闲服装", LevelEnum.THIRD,Clothing,"851324"),
    Shoes_Hats("350205","鞋帽", LevelEnum.THIRD,Clothing,"851325"),
    Home_Textiles("350206","家纺", LevelEnum.THIRD,Clothing,"851326"),
    Other_Clothing("350207","其他服装", LevelEnum.THIRD,Clothing,"851327"),

    Chemical_Raw_Materials("370101","化学原料药", LevelEnum.THIRD,Chemical_Pharmacy,"851511"),
    Chemical_Preparation("370102","化学制剂", LevelEnum.THIRD,Chemical_Pharmacy,"851512"),

    Chinese_Medicine_Level3("370201","中药", LevelEnum.THIRD,Chinese_Medicine,"851521"),

    Biological_Products_Level3("370301","生物制品", LevelEnum.THIRD,Biological_Products,"851531"),
    Medicine_Busi_Level3("370401","医药商业", LevelEnum.THIRD,Medicine_Business,"851541"),
    Medical_Instr_Level3("370501","医疗器械", LevelEnum.THIRD,Medical_Instrument,"851551"),
    Medical_Service_Level3("370601","医疗服务", LevelEnum.THIRD,Medical_Service,"851561"),

    Department_Store("450301","百货", LevelEnum.THIRD,Retail,"852031"),
    Supermarket("450302","超市", LevelEnum.THIRD,Retail,"852032"),
    Multi_Industry_Retail("450303","多业态零售", LevelEnum.THIRD,Retail,"852033"),

    Professional_Chain("450401","专业连锁", LevelEnum.THIRD,Profession_retail,"852041"),
    Property_Bussiness_Level3("450501","一般物业经营", LevelEnum.THIRD,Property_Management,"852051"),
    Professional_Market("450502","专业市场", LevelEnum.THIRD,Property_Management,"852052"),

    Trade_Level3("450201","贸易", LevelEnum.THIRD,Trade,"852021"),

    Artificial_Scenic_Spots("460101","人工景点", LevelEnum.THIRD,Scenic_Spot,"852111"),
    Natural_Scenic_Spots("460102","自然景点", LevelEnum.THIRD,Scenic_Spot,"852112"),

    Hotel_Level3("460201","酒店", LevelEnum.THIRD,Hotel,"852121"),
    Tourism_Integration_Level3("460301","旅游综合", LevelEnum.THIRD,Tourism_Integration,"852131"),
    Restaurant_Level3("460401","餐饮", LevelEnum.THIRD,Restaurant,"852141"),
    Other_Leisure_Service_Level3("460501","其他休闲服务", LevelEnum.THIRD,Other_Leisure_Service,"852151"),

    Integrated_Circuit("270101","集成电路", LevelEnum.THIRD,Semiconductor,"850811"),
    Discrete_Devices("270102","分立器件", LevelEnum.THIRD,Semiconductor,"850812"),
    Semiconductor_Meterial("270103","半导体材料", LevelEnum.THIRD,Semiconductor,"850813"),

    Printed_Circuit_Board("270202","印制电路板", LevelEnum.THIRD,Circuit_Component,"850822"),
    Passive_Element("270203","被动元件", LevelEnum.THIRD,Circuit_Component,"850823"),

    Display_Device("270301","显示器件", LevelEnum.THIRD,Photoelectric,"850831"),
    LED("270302","LED", LevelEnum.THIRD,Photoelectric,"850832"),
    Optical_Element("270303","光学元件", LevelEnum.THIRD,Photoelectric,"850833"),

    Electronic_System_Assembly("270501","电子系统组装", LevelEnum.THIRD,Electronic_Manufacturing,"850851"),
    Electronic__Parts_Manufacturing("270502","电子零部件制造", LevelEnum.THIRD,Electronic_Manufacturing,"850852"),

    Other_Electronics_Level3("270401","其他电子", LevelEnum.THIRD,Other_Electronics,"850841"),

    Computer_Equip_Level3("710101","计算机设备", LevelEnum.THIRD,Computer_Equipment,"851021"),
    Soft_Develop("710201","软件开发", LevelEnum.THIRD,Computer_Application,"852225"),
    IT_Service("710202","IT 服务", LevelEnum.THIRD,Computer_Application,"852226"),

    Printed_Media("720101","平面媒体", LevelEnum.THIRD,Cultural_Media,"852241"),
    Film_Animation("720102","影视动漫", LevelEnum.THIRD,Cultural_Media,"852242"),
    CATV_Network("720103","有线电视网络", LevelEnum.THIRD,Cultural_Media,"852224"),
    Other_Cultural_Media("720104","其他文化传媒", LevelEnum.THIRD,Cultural_Media,"852244"),

    Marketing_Service("720201","营销服务", LevelEnum.THIRD,Marketing_Communication,"852243"),
    Internet_Information_Service("720301","互联网信息服务", LevelEnum.THIRD,Internet_Media,"852221"),
    Mobile_Internet_Service("720302","移动互联网服务", LevelEnum.THIRD,Internet_Media,"852222"),
    Other_Internet_Services("720303","其他互联网服务", LevelEnum.THIRD,Internet_Media,"852223"),


    Commun_Operation_Level3("730101","通信运营", LevelEnum.THIRD,Communication_Operation,"852211"),
    Terminal_Equipment("730201","终端设备", LevelEnum.THIRD,Communication_Equipment,"851012"),
    Cummun_Trans_Equip("730202","通信传输设备", LevelEnum.THIRD,Communication_Equipment,"851013"),
    Commun_Supporting_Equipment("730203","通信配套服务", LevelEnum.THIRD,Communication_Equipment,"851014"),

    Thermal_Power("410101","火电", LevelEnum.THIRD,Power,"851611"),
    Hydropower("410102","水电", LevelEnum.THIRD,Power,"851612"),
    Gas_Turbine_Power("410103","燃机发电", LevelEnum.THIRD,Power,"851613"),
    Themoelectricity("410104","热电", LevelEnum.THIRD,Power,"851614"),
    New_Energy_Power("410105","新能源发电", LevelEnum.THIRD,Power,"851615"),

    Water_Affairs_Level3("410201","水务", LevelEnum.THIRD,Water_Affairs,"851621"),
    Gas_Level3("410301","燃气", LevelEnum.THIRD,Gas,"851631"),
    Envir_Engin_Service_Level3("410401","环保工程及服务", LevelEnum.THIRD,Environmantal_Engineering_Service,"851641"),

    Port_Level3("420101","港口", LevelEnum.THIRD,Port,"851711"),
    Expressway_Level3("420201","高速公路", LevelEnum.THIRD,Expressway,"851731"),
    Transit_Level3("420301","公交", LevelEnum.THIRD,Transit,"851721"),
    Air_Transport_Level3("420401","航空运输", LevelEnum.THIRD,Air_Transport,"851741"),
    Airport_Level3("420501","机场", LevelEnum.THIRD,Airport,"851751"),
    Shipping_Level3("420601","航运", LevelEnum.THIRD,Shipping,"851761"),
    Railway_Transport_Level3("420701","铁路运输", LevelEnum.THIRD,Railway_Transportation,"851771"),
    Logistics_Level3("420801","物流", LevelEnum.THIRD,Logistics,"851781"),

    Real_Estate_Level3("430101","房地产开发", LevelEnum.THIRD,Real_Estate_Development,"851811"),
    Park_Develop_Level3("430201","园区开发", LevelEnum.THIRD,Park_Development,"851821"),

    Bank_Level3("480101","银行", LevelEnum.THIRD,Bank_Secondary,"851911"),

    Security_Level3("490101","证券", LevelEnum.THIRD,Security_Bound,"851931"),
    Insurance_Level3("490201","保险", LevelEnum.THIRD,Insurance,"851941"),
    Diversified_Finance_Level3("490301","多元金融", LevelEnum.THIRD,Diversified_Finance,"851921"),
    Muptiple_Level3("510101","综合", LevelEnum.THIRD,Muptiple,"852311");
    private String symbol;

    private String name;
    private LevelEnum level;
    private IndustryEnum parentEnum;
    private String index;

    IndustryEnum(String symbol, String name, LevelEnum level, IndustryEnum parentEnum,String index) {
        this.index = index;
        this.name = name;
        this.level = level;
        this.parentEnum = parentEnum;
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public IndustryEnum getParentEnum() {
        return parentEnum;
    }

    public void setParentEnum(IndustryEnum parentEnum) {
        this.parentEnum = parentEnum;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private enum LevelEnum{
        FIRST,SECOND,THIRD;
    }

}
