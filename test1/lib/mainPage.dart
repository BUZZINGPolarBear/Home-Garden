import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:test1/API.dart';
import 'package:test1/History.dart';
import 'package:test1/HomePage.dart';
import 'package:test1/Mode.dart';
import 'package:test1/Sign_up.dart';
import 'package:test1/control.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;


class userID {
  String userId= '';
  String userMode = '';
  String nickname = '';

  userID(this.userId, this.userMode, this.nickname);
}

class innerData {
  String img;
  String humid;
  String illuminate;
  String waterDepth;
  String ph;

  innerData(this.img, this.humid, this.illuminate, this.waterDepth, this.ph);

}


class mainPage extends StatefulWidget {

  final userID userid;
  mainPage({required this.userid});

  @override
  State<mainPage> createState() => _mainPage();
}

class _mainPage extends State<mainPage>{

  int _counter = 0;
  int _currentIndex = 0;

  bool _isChecked1 = false;
  bool _isChecked2 = false;
  String humidity = '0';
  String illum = '0';
  String ph = '0';
  String depth = '0';
  String bottomSelect = '0';
  String mode = 'C';





  void _onTap (int index) async {
    setState(() {
      _currentIndex = index;
      print(_currentIndex);
    });
    if  (_currentIndex == 0) { // 현재 작물 정보 가져오기
      // innerData in = getData(widget.userid.userId);

    }
  }


  Widget build (BuildContext context) {

    final List<Widget> _children = [
      home(title: widget.userid.nickname,),
      myHistory(title: widget.userid.nickname, ), // 오늘 날짜 전달?
      myMode(title: widget.userid.nickname,), // 현재 모드 전달
      myControl(title: widget.userid.nickname)]; // 현재 장치 모드 전달
    return Scaffold(
      body: _children[_currentIndex],

      bottomNavigationBar: BottomNavigationBar(
        fixedColor: Color(0xffF3E5F5),
        type: BottomNavigationBarType.fixed,
        onTap: _onTap,
        currentIndex: _currentIndex,
        items: [
          new BottomNavigationBarItem(
            icon: Icon(Icons.home),
            title: Text('home'),
          ),
          new BottomNavigationBarItem(
            icon: Icon(Icons.calendar_view_day),
            title: Text('history'),
          ),

          new BottomNavigationBarItem(
            icon: Icon(Icons.mode),
            title: Text('mode'),
          ),

          new BottomNavigationBarItem(
            icon: Icon(Icons.control_point),
            title: Text('controll'),
          ),
        ],
      ),
    );
  }
}