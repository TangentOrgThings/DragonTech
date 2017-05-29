// vim :set ts=2 sw=2 sts=2 expandtab smarttab :
/**
 *  HomeSeer HS-WD100+
 *
 *  Copyright 2016 DarwinsDen.com
 *
 *  For device parameter information and images, questions or to provide feedback on this device handler, 
 *  please visit: 
 *
 *      darwinsden.com/homeseer100plus/
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Author: Darwin@DarwinsDen.com
 *	Date: 2016-04-10
 *
 *	Changelog:
 *
 *	0.10 (04/10/2016) -	Initial 0.1 Beta.
 *  0.11 (05/28/2016) - Set numberOfButtons attribute for ease of use with CoRE and other SmartApps. Corrected physical/digital states.
 *  0.12 (06/03/2016) - Added press type indicator to display last tap/hold press status
 *  0.13 (06/13/2016) - Added dim level ramp-up option for remote dim commands
 *  0.14 (08/01/2016) - Corrected 60% dim rate limit test that was inadvertently pulled into repository
 *  0.15 (09/06/2016) - Added Firmware version info. Removed unused lit indicator button.
 *  0.16 (09/24/2016) - Added double-tap-up to full brightness option and support for firmware dim rate configuration parameters.
 *  0.17 (10/05/2016) - Added single-tap-up to full brightness option.
 *
 */
 

def getDriverVersion() {
  return "3.91"
}

def getAssociationGroup() {
  return 1
}

metadata {
  definition (name: "WD100+ Dimmer", namespace: "TangentOrgThings", author: "brian@tangent.org") {
    capability "Actuator"
    capability "Button"
    capability "Health Check"
    capability "Illuminance Measurement"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch Level"
    capability "Switch"
    capability "Light"
    
    command "tapUp2"
    command "tapDown2"
    command "tapUp3"
    command "tapDown3"
    command "holdUp"
    command "holdDown"
   
    attribute "Associated", "string"
    attribute "driverVersion", "string"
    attribute "FirmwareMdReport", "string"
    attribute "firmwareVersion", "string"
    attribute "Manufacturer", "string"
    attribute "ManufacturerCode", "string"
    attribute "MSR", "string"
    attribute "ProduceTypeCode", "string"
    attribute "ProductCode", "string"
    attribute "WakeUp", "string"
    attribute "WirelessConfig", "string"

    // 0 0 0x2001 0 0 0 a 0x30 0x71 0x72 0x86 0x85 0x84 0x80 0x70 0xEF 0x20
    // zw:L type:1101 mfr:0184 prod:4447 model:3034 ver:5.14 zwv:4.24 lib:03 cc:5E,86,72,5A,85,59,73,26,27,70,2C,2B,5B,7A ccOut:5B role:05 ff:8600 ui:8600
    fingerprint type:"0600", mfr: "0184", model: "3034", cc: "5E, 86, 72, 5A, 85, 59, 73, 26, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "WD100+ In-Wall Dimmer"
    fingerprint type:"0600", mfr: "0184", model: "3034", prod: "4447", cc: "5E, 86, 72, 5A, 85, 59, 73, 26, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "WD100+ In-Wall Dimmer"
    fingerprint type:"0600", mfr: "000C", model: "3034", prod: "4447", cc: "5E, 86, 72, 5A, 85, 59, 73, 26, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "HS-WD100+ In-Wall Dimmer"
  }

  simulator {
    status "on":  "command: 2003, payload: FF"
    status "off": "command: 2003, payload: 00"
    status "09%": "command: 2003, payload: 09"
    status "10%": "command: 2003, payload: 0A"
    status "33%": "command: 2003, payload: 21"
    status "66%": "command: 2003, payload: 42"
    status "99%": "command: 2003, payload: 63"

    // reply messages
    reply "2001FF,delay 5000,2602": "command: 2603, payload: FF"
    reply "200100,delay 5000,2602": "command: 2603, payload: 00"
    reply "200119,delay 5000,2602": "command: 2603, payload: 19"
    reply "200132,delay 5000,2602": "command: 2603, payload: 32"
    reply "20014B,delay 5000,2602": "command: 2603, payload: 4B"
    reply "200163,delay 5000,2602": "command: 2603, payload: 63"
  }

  preferences {      
    input "doubleTapToFullBright", "bool", title: "Double-Tap Up sets to full brightness",  defaultValue: false,  displayDuringSetup: true, required: false	       
    input "singleTapToFullBright", "bool", title: "Single-Tap Up sets to full brightness",  defaultValue: false,  displayDuringSetup: true, required: false	       
    input "reverseSwitch", "bool", title: "Reverse Switch",  defaultValue: false,  displayDuringSetup: true, required: false	       

    input ( "localStepDuration", "number", title: "Press Configuration button after entering ramp rate preferences\n\nLocal Ramp Rate: Duration of each level (1-22)(1=10ms) [default: 3]", defaultValue: 3,range: "1..22", required: false)
    input ( "localStepSize", "number", title: "Local Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", defaultValue: 1, range: "1..99", required: false)
    input ( "remoteStepDuration", "number", title: "Remote Ramp Rate: Duration of each level (1-22)(1=10ms) [default: 3]", defaultValue: 3,range: "1..22", required: false)
    input ( "remoteStepSize", "number", title: "Remote Ramp Rate: Dim level % to change each duration (1-99) [default: 1]", defaultValue: 1, range: "1..99", required: false)
  }
    
  tiles(scale: 2) {
    multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
	  tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState "on", label:'${name}', action:"switch.off", icon:"st.Home.home30", backgroundColor:"#79b821", nextState:"turningOff"
        attributeState "off", label:'${name}', action:"switch.on", icon:"st.Home.home30", backgroundColor:"#ffffff", nextState:"turningOn"
        attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.Home.home30", backgroundColor:"#79b821", nextState:"turningOff"
        attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.Home.home30", backgroundColor:"#ffffff", nextState:"turningOn"
      }
      tileAttribute ("device.level", key: "SLIDER_CONTROL") {
        attributeState "level", action:"switch level.setLevel"
      }
      tileAttribute("device.status", key: "SECONDARY_CONTROL") {
        attributeState("default", label:'${currentValue}', unit:"")
      }
    }

    standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
    }

    valueTile("firmwareVersion", "device.firmwareVersion", width:2, height: 2, decoration: "flat", inactiveLabel: true) {
      state "default", label: '${currentValue}'
    }

    valueTile("driverVersion", "device.driverVersion", inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }
  
    valueTile("level", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
      state "level", label:'${currentValue} %', unit:"%", backgroundColor:"#ffffff"
    }

    standardTile("tapUp2", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▲▲", backgroundColor: "#ffffff", action: "tapUp2", icon: "st.Home.home30"
    }     

    standardTile("tapDown2", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▼▼", backgroundColor: "#ffffff", action: "tapDown2", icon: "st.Home.home30"
    } 

    standardTile("tapUp3", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▲▲▲", backgroundColor: "#ffffff", action: "tapUp3", icon: "st.Home.home30"
    } 

    standardTile("tapDown3", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▼▼▼", backgroundColor: "#ffffff", action: "tapDown3", icon: "st.Home.home30"
    } 

    standardTile("holdUp", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Hold ▲", backgroundColor: "#ffffff", action: "holdUp", icon: "st.Home.home30"
    } 

    standardTile("holdDown", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Hold ▼", backgroundColor: "#ffffff", action: "holdDown", icon: "st.Home.home30"
    }
      
    main(["switch"])
    details(["switch", "tapUp2", "tapUp3", "holdUp", "tapDown2", "tapDown3", "holdDown", "level", "driverVersion", "refresh"])
  }
}

def parse(String description) {
  def result = null

  log.debug "PARSE: ${description}"
  if (description.startsWith("Err")) {
    if (description.startsWith("Err 106")) {
      if (state.sec) {
        log.debug description
      } else {
        result = createEvent(
          descriptionText: "This device failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
          eventType: "ALERT",
          name: "secureInclusion",
          value: "failed",
          isStateChange: true,
        )
      }
    } else {
      result = createEvent(value: description, descriptionText: description)
    }
  } else if (description != "updated") {
    def cmd = zwave.parse(description)
	
    if (cmd) {
      result = zwaveEvent(cmd)
      
      if (!result) {
        log.warning "Parse Failed and returned ${result} for command ${cmd}"
        result = createEvent(value: description, descriptionText: description)
      } else {
        // If we displayed the result
      }
    } else {
      log.info "Non-parsed event: ${description}"
      result = createEvent(value: description, descriptionText: description)
    }
  }
    
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
  dimmerEvents(cmd) 
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
  dimmerEvents(cmd) 
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelSet cmd) {
  dimmerEvents(cmd)   
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {        
  dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) { //physical device events ON/OFF
  dimmerEvents(cmd)
}

private dimmerEvents(physicalgraph.zwave.Command cmd) {
  def value = (cmd.value ? "on" : "off")
   
  def result = [createEvent(name: "switch", value: value)]
  // state.lastLevel = cmd.value
  if (cmd.value && cmd.value <= 100) {
    result << createEvent(name: "level", value: cmd.value, unit: "%")
  }
  
  result << createEvent(name: "illuminanceMeasurement", value: cmd.value, unit: "%")
  
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
  log.debug "ConfigurationReport $cmd"
  /*
  def value = "when off"
  if (cmd.configurationValue[0] == 1) { value = "when on" }
  if (cmd.configurationValue[0] == 2) { value = "never" }
  
  createEvent(name: "indicatorStatus", value: value)
  */
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd) {
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  updateDataValue("FirmwareMdReport", firmware_report)
  [createEvent([name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", isStateChange: false])]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelStopLevelChange cmd) {
  [createEvent(name:"switch", value:"on"), response(command(zwave.switchMultilevelV1.switchMultilevelGet()))]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelStopLevelChange cmd) {
  [createEvent(name:"switch", value:"on"), response(command(zwave.switchMultilevelV1.switchMultilevelGet()))]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  createEvent(descriptionText: "$device.displayName command not implemented: $cmd", displayed: true)
}

def on() {
  commands([
    zwave.basicV1.basicSet(value: 0xFF),
    zwave.switchMultilevelV1.switchMultilevelGet()
   ], 5000)
}

def off() {
  commands([
    zwave.basicV1.basicSet(value: 0x00),
	zwave.switchMultilevelV1.switchMultilevelGet()
  ], 5000)
}

def setLevel (value) {
  log.info "setLevel >> value: $value"
  def valueaux = value as Integer
  def level = Math.max(Math.min(valueaux, 99), 0)
  if (level > 0) {
    sendEvent(name: "switch", value: "on")
  } else {
    sendEvent(name: "switch", value: "off")
  }
  sendEvent(name: "level", value: level, unit: "%")

  commands([
    zwave.basicV1.basicSet(value: level),
    zwave.switchMultilevelV1.switchMultilevelGet()
  ], 5000)
}

def setLevel(value, duration) {
  log.debug "setLevel >> value: $value, duration: $duration"
  def valueaux = value as Integer
  def level = Math.max(Math.min(valueaux, 99), 0)
  def dimmingDuration = duration < 128 ? duration : 128 + Math.round(duration / 60)
  def getStatusDelay = duration < 128 ? (duration*1000)+2000 : (Math.round(duration / 60)*60*1000)+2000
  commands([
    zwave.switchMultilevelV1.switchMultilevelSet(value: level, dimmingDuration: dimmingDuration),
    zwave.switchMultilevelV1.switchMultilevelGet()
  ], getStatusDelay)
}

/**
  * PING is used by Device-Watch in attempt to reach the Device
**/
def ping() {
    refresh()
}

def refresh() {
  command(zwave.switchMultilevelV1.switchMultilevelGet())
}

def poll() {
  // sendHubCommand(new physicalgraph.device.HubAction(zwave.switchBinaryV1.switchBinaryGet().format()))
  // sendHubCommand(new physicalgraph.device.HubAction(zwave.switchMultilevelV1.switchMultilevelGet().format()))
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def zwaveEvent(physicalgraph.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
  def result = []
  
  result << createEvent(descriptionText: cmd.toString(), isStateChange: true, displayed: true)
  result << response(command(zwave.associationV2.associationGet(groupingIdentifier: 1)))
  
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug("sceneNumber: ${cmd.sceneNumber} keyAttributes: ${cmd.keyAttributes}")
  def result = []

  switch (cmd.sceneNumber) {
    case 1:
          // Up
          switch (cmd.keyAttributes) {
            case 0:
                result += createEvent([name: "switch", value: "on", type: "physical"])
                if (singleTapToFullBright) {
                  result += setLevel(99)
                  result += response("delay 5000")
                  result += response(zwave.switchMultilevelV1.switchMultilevelGet())
                }
                break
            case 1:
                result=createEvent([name: "switch", value: "on", type: "physical"])
                break
            case 2:
                // Hold
                result += createEvent(holdUpResponse("physical"))  
                result += createEvent([name: "switch", value: "on", type: "physical"])
                break
            case 3: 
                // 2 Times
                result +=createEvent(tapUp2Response("physical"))
                if (doubleTapToFullBright)
                {
                   result += setLevel(99)
                   result += response("delay 5000")
                   result += response(zwave.switchMultilevelV1.switchMultilevelGet())
                }                    
                break
            case 4:
                // 3 Three times
                result=createEvent(tapUp3Response("physical"))
                break
            default:
                log.debug ("unexpected up press keyAttribute: $cmd.keyAttributes")
        }
        break
          
    case 2:
        // Down
        switch (cmd.keyAttributes) {
            case 0:
                  result=createEvent([name: "switch", value: "off", type: "physical"])
                  break

              case 1:
                  result=createEvent([name: "switch", value: "off", type: "physical"])
                  break
              case 2:
                  // Hold
                  result += createEvent(holdDownResponse("physical"))
                  result += createEvent([name: "switch", value: "off", type: "physical"]) 
                  break
              case 3: 
                  // 2 Times
                  result +=createEvent(tapDown2Response("physical"))
                  result += response("delay 5000")
                  result += response(zwave.switchMultilevelV1.switchMultilevelGet())
                  break
              case 4:
                  // 3 Times
                  result=createEvent(tapDown3Response("physical"))
                  break
              default:
                  log.debug ("unexpected down press keyAttribute: $cmd.keyAttributes")
           } 
           break
           
      default:
           // unexpected case
           log.debug ("unexpected scene: $cmd.sceneNumber")
   }
   
   return result
}

def tapUp2Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲")
  [name: "button", value: "pushed", data: [buttonNumber: "1"], descriptionText: "$device.displayName Tap-Up-2 (button 1) pressed", isStateChange: true, type: "$buttonType"]
}

def tapDown2Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼▼")
  [name: "button", value: "pushed", data: [buttonNumber: "2"], descriptionText: "$device.displayName Tap-Down-2 (button 2) pressed", isStateChange: true, type: "$buttonType"]
}

def tapUp3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲▲")
  [name: "button", value: "pushed", data: [buttonNumber: "3"], descriptionText: "$device.displayName Tap-Up-3 (button 3) pressed", isStateChange: true, type: "$buttonType"]
}

def tapDown3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▼▼▼")
  [name: "button", value: "pushed", data: [buttonNumber: "4"], descriptionText: "$device.displayName Tap-Down-3 (button 4) pressed", isStateChange: true, type: "$buttonType"]
}

def holdUpResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▲")
  [name: "button", value: "pushed", data: [buttonNumber: "5"], descriptionText: "$device.displayName Hold-Up (button 5) pressed", isStateChange: true, type: "$buttonType"]
}

def holdDownResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▼")
  [name: "button", value: "pushed", data: [buttonNumber: "6"], descriptionText: "$device.displayName Hold-Down (button 6) pressed", isStateChange: true, type: "$buttonType"]
}

def tapUp2() {
  sendEvent(tapUp2Response("digital"))
}

def tapDown2() {
  sendEvent(tapDown2Response("digital"))
}

def tapUp3() {
  sendEvent(tapUp3Response("digital"))
}

def tapDown3() {
  sendEvent(tapDown3Response("digital"))
}

def holdUp() {
  sendEvent(holdUpResponse("digital"))
}

def holdDown() {
  sendEvent(holdDownResponse("digital"))
} 

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
  def result = []
  
  log.debug ("AssociationReport()")
  
  if (cmd.groupingIdentifier == getAssociationGroup()) {
    def string_of_assoc = ""
    cmd.nodeId.each {
      string_of_assoc += "${it}, "
    }
    def lengthMinus2 = string_of_assoc.length() - 3
    def final_string = string_of_assoc.getAt(0..lengthMinus2)
    
    if (cmd.nodeId.any { it == zwaveHubNodeId }) {
      Boolean isStateChange = state.isAssociated ?: false
      result << createEvent(name: "Associated",
                            value: "${final_string}", 
                            descriptionText: "${final_string}",
                            displayed: true,
                            isStateChange: isStateChange)
      
      state.isAssociated = true
    } else {
      Boolean isStateChange = state.isAssociated ? true : false
      result << createEvent(name: "Associated",
                          value: "",
                          descriptionText: "${final_string}",
                          displayed: true,
                          isStateChange: isStateChange)
      state.isAssociated = false
    } 
  } else {
    Boolean isStateChange = state.isAssociated ? true : false
    result << createEvent(name: "Associated",
                          value: "misconfigured",
                          descriptionText: "misconfigured group ${cmd.groupingIdentifier}",
                          displayed: true,
                          isStateChange: isStateChange)
  }
  
  if (state.isAssociated == false) {
    result << response(commands([ 
                                  zwave.associationV2.associationSet(groupingIdentifier: getAssociationGroup(), nodeId: [zwaveHubNodeId]),
                                  zwave.associationV2.associationGet(groupingIdentifier: getAssociationGroup())
                                  ], 1000))
  }
    
  return result
}

def setConfigured() {
  [
    setDimRatePrefs(),
    zwave.versionV1.versionGet(),
    zwave.firmwareUpdateMdV2.firmwareMdGet(),
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    zwave.associationV2.associationSet(groupingIdentifier: getAssociationGroup(), nodeId:[zwaveHubNodeId]),
    zwave.associationV2.associationGet(groupingIdentifier: getAssociationGroup())
  ]
}

def installed() {
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed:true)
  response(commands(setConfigured(), 2000))
}

def setDimRatePrefs() {
  def cmds = []

  if (remoteStepSize) {
    def remoteStepSize = Math.max(Math.min(remoteStepSize, 99), 1)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [remoteStepSize], parameterNumber: 7, size: 1)
  }
   
  if (remoteStepDuration) {
    def remoteStepDuration = Math.max(Math.min(remoteStepDuration, 22), 2)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0, remoteStepDuration], parameterNumber: 8, size: 2)
  }
   
   
  if (localStepSize) {
    def localStepSize = Math.max(Math.min(localStepSize, 99), 1)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [localStepSize], parameterNumber: 9, size: 1)
  }
   
  if (localStepDuration) {
    def localStepDuration = Math.max(Math.min(localStepDuration, 22), 2)
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0,localStepDuration], parameterNumber: 10, size: 2)
  }
   
  if (reverseSwitch) {
    cmds << zwave.configurationV2.configurationSet(configurationValue: [1], parameterNumber: 4, size: 1)
  } else {
    cmds << zwave.configurationV2.configurationSet(configurationValue: [0], parameterNumber: 4, size: 1)
  }
   
  return cmds
}
 
def updated() {
  log.debug "$device.displayName updated"

  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
  /*
  response(commands(setConfigured(), 2000))
  sendHubCommand(new physicalgraph.device.HubAction(zwave.switchBinaryV1.switchBinaryGet().format()))
  */
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed:true)
  state.driverVersion = getDriverVersion()
}

private command(physicalgraph.zwave.Command cmd) {
  if (state.sec) {
    zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
  } else {
    cmd.format()
  }
}
private commands(commands) {
  delayBetween(commands.collect{ command(it) })
}

private commands(commands, delay) {
  delayBetween(commands.collect{ command(it) }, delay)
}
