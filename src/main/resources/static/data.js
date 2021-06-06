function setPointColor(name){
    if(name === 'D'){
        return "rgb(160,13,15)";
    }else{
        return "rgb(15,254,15)";
    }
}
function parseFX(fx){
    if(fx == null){
        return null;
    }
    return {
        name: String(fx.fx),
        coord: [fx.time,(fx.mark=== 'G' ? fx.fx*1.001 : fx.fx * 0.999)],
        itemStyle :{
            color:setPointColor(fx.mark)
        },
        label: {
            show: true,
            formatter:String(fx.fx),
            fontWeight:'bold',
            fontSize:15
        },
        symbol: 'diamond',
        symbolSize: 10,
    };
}
function markDataPoint(fxs){
    if(fxs == null){
        return null;
    }
    let data = []
    for (let i=0;i<fxs.length;i++){
        let point = parseFX(fxs[i])
        data.push(point);
    }
    return data;
}
function markDataLine(fxs){
    let line = []
    let point = markDataPoint(fxs);
    if (point == null){
        return [];
    }
    for (let i = 0;i<point.length -1;i++){
        point[i].symbol = 'none';
        point[i].label.show = false;
        point[i+1].symbol = 'none';
        line.push([point[i],point[i+1]]);
    }
    return line;
}

function addXDLine(fxs,bisCenter,xdData){
    if(fxs == null|| bisCenter == null){
        return null;
    }
    let line = markDataLine(fxs);
    if (xdData != null){
        for (let i=0;i<xdData.length;i++){
            let point1 = parseFX(xdData[i].beginFX);
            let point2 = parseFX(xdData[i].endFX);
            point1.label.show = false;
            point2.label.show = false;
            line.push([point1,point2]);
        }
        let line1 = {
            name: '线段中枢高点',
            yAxis: xdData[xdData.length - 1].high,
            lineStyle: {
                normal: {
                    color: "green",
                    width: 2,
                    type: 'solid'
                }
            },
            label:{
                show:true,
                formatter:xdData[xdData.length - 1].high
            }
        };
        let line2 = {
            name: '线段中枢低点',
            yAxis: xdData[xdData.length - 1].low,
            lineStyle: {
                normal: {
                    color: "red",
                    width: 2,
                    type: 'solid'
                }
            },
            label:{
                show:true,
                formatter:xdData[xdData.length - 1].low
            }
        };
        line.push(line1);
        line.push(line2);
    }
    let line3 = {
        name:'最近笔中枢高点',
        yAxis: bisCenter.high,
        lineStyle:{
            normal:{
                color:"green",
                width:1,
                type:'solid'
            }
        },
        label:{
            show:true,
            formatter:bisCenter.high,
        }
    }
    let line4 ={
        name:'最近笔中枢低点',
        yAxis: bisCenter.low,
        lineStyle:{
            normal:{
                color:"red",
                width:1,
                type:'solid'
            }
        },
        label:{
            show:true,
            formatter:bisCenter.low,
        }
    }
    line.push(line3);
    line.push(line4);
    //alert(JSON.stringify(line));
    return line;
}
function splitData(rawData) {
    let categoryData = [];
    let values = [];
    let volumes = [];
    for (let i = 0; i < rawData.length; i++) {
        categoryData.push(rawData[i].splice(0, 1)[0]);
        values.push(rawData[i]);
        volumes.push([i, rawData[i][4], rawData[i][0] > rawData[i][1] ? 1 : -1]);
    }
    return {
        categoryData: categoryData,
        values: values,
        volumes: volumes
    };
}
function calculateMA(dayCount,data) {
    const result = [];
    for (let i = 0, len = data.values.length; i < len; i++) {
        if (i < dayCount) {
            result.push('-');
            continue;
        }
        let sum = 0;
        for (let j = 0; j < dayCount; j++) {
            sum += parseFloat(data.values[i - j][1]);
        }
        result.push((sum / dayCount).toFixed(3));
    }
    return result;
}
function options(analyze,data,color_0,macd,constant){
    let freq=constant.freq;
    let title = constant.title;
    let fxs = analyze.fxs;
    let bisCenter = analyze.bisCenter;
    let xdData = analyze.xdData;
    let upColor = color_0.upColor;
    let downColor = color_0.downColor;
    let macds = macd.macds;
    let diffs = macd.diffs;
    let deas = macd.deas;
    option = {
        title: {
            text: title,
            left: 150
        },
        animation: false,
        legend: {
            top: 10,
            left: 'center',
            data: [freq, 'MA5', 'MA10', 'MA20', 'MA30']
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                lineStyle: {
                    color: '#376df4',
                    width: 2,
                    opacity: 1
                }
            },
            borderWidth: 1,
            borderColor: '#ccc',
            padding: 10,
            borderRadius:4,
            textStyle: {
                color: '#000'
            }
        },
        axisPointer: {//指定鼠标位置的十字的情况
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: false
                },
                brush: {
                    type: ['lineX', 'clear']
                }
            }
        },
        brush: { //用于选择框，选中区域意外的透明度降低
            xAxisIndex: 'all',
            brushLink: 'all',
            outOfBrush: {
                colorAlpha: 0.1
            }
        },
        //与成交量的现实相关，如果被注释掉，则成交量注视图是蓝色的。
        visualMap: [
            {
                show: false,
                seriesIndex: 5,//第5个数据
                dimension: 2,
                pieces: [{
                    value: 1,
                    color: downColor
                }, {
                    value: -1,
                    color: upColor
                }],
            }
        ],
        grid: [//指定图表被分成几个部分
            {
                left: '5%',
                right: '5%',
                height: '50%',
                borderColor:'#ccc',//好像不起作用
                backgroundColor:'#efefef',//好像不起作用
                borderWidth:1//好像不起作用
            },{
                left: '5%',
                right: '5%',
                top: '61%',
                height: '10%',
                borderColor:'#ccc',
                borderWidth:1
            },{
                left: '5%',
                right: '5%',
                top: '72%',
                height: '14%',
                borderColor:'#ccc',
                borderWidth:1
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: data.categoryData,
                scale: true,
                boundaryGap: true,//如果为false，则最前和最后一个K线有一半在外边
                axisLine: {onZero: false},
                splitLine: {show: false},
                splitNumber: 20,
                min: 'dataMin',
                max: 'dataMax',
                axisPointer: {
                    z: 100
                }
            },
            {
                type: 'category',
                gridIndex: 1,
                data: data.categoryData,
                scale: true,
                boundaryGap: true,
                axisLine: {onZero: false},
                axisTick: {show: false},
                splitLine: {show: false},
                axisLabel: {show: false},
                splitNumber: 20,
                min: 'dataMin',
                max: 'dataMax'
            },
            {
                type:"category",
                gridIndex:2,
                boundaryGap: true,
                data:data.categoryData,
                axisLabel:{show:false},
                axisTick: {show: false},
            }
        ],
        yAxis: [
            {
                max: function(value){
                    return value.max *1.01;
                },
                min: function(value){
                    return value.min * 0.99;
                },
                scale: true,
                splitArea: {
                    show: true
                }
            },
            {
                scale: true,
                gridIndex: 1,
                splitNumber: 3,
                axisLabel: {
                    show: true,
                    formatter:'{value}手',
                },
                axisLine: {onZero:false},//应该是从0开始，比如45，50，坐标的起点仍为0
                axisTick: {show: false},
                splitLine: {show: false}
            },
            {
                gridIndex:2,
                splitNumber:4,
                axisLabel: {show: true},
                axisLine: {onZero:false},
                axisTick: {show:false},
                splitLine:{show:false}//分成4部分，分割线是否显示
            }

        ],
        dataZoom: [
            {
                type: 'inside',
                xAxisIndex: [0, 0],
            },
            {
                show: true,
                xAxisIndex: [0, 1],//控制x坐标轴的第一个和第二个
                type: 'slider',
                top: '87%',
            },{
                show:false,
                xAxisIndex:[0,2],
                type:"inside",
            }
        ],
        series: [
            {
                name: freq,
                type: 'candlestick',
                data: data.values,
                itemStyle: {
                    color: upColor,
                    color0: downColor,
                    borderColor: null,
                    borderColor0: null
                },
                markPoint: {
                    label: {
                        normal: {
                            formatter: function (param) {
                                return param != null ? param.mark : '';
                            }
                        }
                    },
                    data:markDataPoint(fxs),
                    tooltip: {
                        formatter: function (param) {
                            return param.name + '<br>' + (param.data.coord || '');
                        }
                    }
                },
                markLine: {
                    symbol: ['none', 'none'],
                    data: addXDLine(fxs,bisCenter,xdData),
                },
                tooltip: {
                    formatter: function (param) {
                        param = param[0];
                        return [
                            'Date: ' + param.name + '<hr size=1 style="margin: 3px 0">',
                            'Open: ' + param.data[0] + '<br/>',
                            'Close: ' + param.data[1] + '<br/>',
                            'Lowest: ' + param.data[2] + '<br/>',
                            'Highest: ' + param.data[3] + '<br/>'
                        ].join('');
                    }
                }
            },
            {
                name: 'MA5',
                type: 'line',
                data: calculateMA(5, data),
                smooth: true,
                symbol:"circle",
                symbolSize:1,
                lineStyle: {
                    width: 1,
                    opacity: 0.5
                }
            },
            {
                name: 'MA10',
                type: 'line',
                data: calculateMA(10, data),
                smooth: true,
                symbol:"circle",
                symbolSize:1,
                lineStyle: {
                    width: 1,
                    opacity: 0.5
                }
            },
            {
                name: 'MA20',
                type: 'line',
                data: calculateMA(20, data),
                smooth: true,
                symbol:"circle",
                symbolSize:1,
                lineStyle: {
                    width: 1,
                    opacity: 0.5
                }
            },
            {
                name: 'MA30',
                type: 'line',
                data: calculateMA(30, data),
                smooth: true,
                symbol:"circle",
                symbolSize:1,
                lineStyle: {
                    width: 1,
                    opacity: 0.5
                }
            },
            {
                name: 'Volume',
                type: 'bar',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: data.volumes,
            },
            {
                name:'MACD',
                type:'bar',
                barWidth:2,
                xAxisIndex:2,
                yAxisIndex:2,
                data:macds,
                itemStyle: {
                    normal: {
                        color: function(params) {
                            var colorList;
                            if (params.data >= 0) {
                                colorList = upColor;
                            } else {
                                colorList = downColor;
                            }
                            return colorList;
                        },
                    }
                }
            },
            {
                name:'DIFF',
                type:"line",
                xAxisIndex:2,
                yAxisIndex:2,
                data:diffs,
                symbol:"circle",
                symbolSize:1,
                lineStyle: {
                    color:'#D2691E',
                    width: 1,
                }
            },
            {
                name:"DEA",
                type:'line',
                xAxisIndex:2,
                yAxisIndex:2,
                data:deas,
                symbol:"circle",
                symbolSize:1,
                lineStyle: {
                    color:'#0000ff',
                    width: 1,
                }
            }
        ]
    };
    return option;
}