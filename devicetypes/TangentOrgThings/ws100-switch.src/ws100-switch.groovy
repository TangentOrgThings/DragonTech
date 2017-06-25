// vim :set ts=2 sw=2 sts=2 expandtab smarttab :
/**
 *  WS-100+ Dragon Tech Industrial, Ltd.
 *
 *  Copyright 2017 Brian Aker <brian@tangent.org>, DarwinsDen.com
 *
 *  For device parameter information and images, questions or to provide feedback on this device handler, 
 *  please visit: 
 *
 *      github.com/TangentOrgThings/ws100plus/
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
 *	Author: Brian Aker <brian@tangent.org>
 *	Date: 2017
 *
 *	Changelog:
 *
 *	2.?? (??/??/2017) -	
 *
 *
 */
 
def getDriverVersion () {
	return "3.41"
}

def getAssociationGroup () {
  return 1
}

metadata {
  definition (name: "WS100+ Switch", namespace: "TangentOrgThings", author: "brian@tangent.org"){
    capability "Actuator"
    capability "Button"
    capability "Polling"
    // capability "Health Check"
    capability "Indicator"
    capability "Refresh"
    capability "Sensor"
    capability "Switch"
    capability "Light"
    capability "Configuration"

    command "tapUp2"
    command "tapDown2"
    command "tapUp3"
    command "tapDown3"
    command "holdUp"
    command "holdDown"
    
    attribute "Associated", "string"
    attribute "driverVersion", "string"
    attribute "FirmwareMdReport", "string"
    attribute "Manufacturer", "string"
    attribute "ManufacturerCode", "string"
    attribute "MSR", "string"
    attribute "ProduceTypeCode", "string"
    attribute "ProductCode", "string"
    attribute "WakeUp", "string"
    attribute "WirelessConfig", "string"

    // zw:L type:1001 mfr:000C prod:4447 model:3033 ver:5.14 zwv:4.05 lib:03 cc:5E,86,72,5A,85,59,73,25,27,70,2C,2B,5B,7A ccOut:5B role:05 ff:8700 ui:8700
    fingerprint mfr: "0184", type: "1001", prod: "4447", model: "3033", cc: "5E, 86, 72, 5A, 85, 59, 73, 25, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "WS100+ On-Off Switch"
    fingerprint mfr: "000C", type: "1001", prod: "4447", model: "3033", cc: "5E, 86, 72, 5A, 85, 59, 73, 25, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B", deviceJoinName: "HS-WS100+ On-Off Switch"
  }

  // simulator metadata
  simulator {
    status "on":  "command: 2003, payload: FF"
    status "off": "command: 2003, payload: 00"

    // reply messages
    reply "2001FF,delay 100,2502": "command: 2503, payload: FF"
    reply "200100,delay 100,2502": "command: 2503, payload: 00"
  }

  tiles(scale: 2) {
    multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState "on", label: '${name}', action: "switch.off", icon: "st.Home.home30", backgroundColor: "#79b821"
        attributeState "off", label: '${name}', action: "switch.on", icon: "st.Home.home30", backgroundColor: "#ffffff"
      }
      tileAttribute("device.status", key: "SECONDARY_CONTROL") {
        attributeState("default", label:'${currentValue}', unit:"")
      }
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

    standardTile("indicator", "device.indicatorStatus", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "when off", action:"indicator.indicatorWhenOn", icon:"st.indicators.lit-when-off"
      state "when on", action:"indicator.indicatorNever", icon:"st.indicators.lit-when-on"
      state "never", action:"indicator.indicatorWhenOff", icon:"st.indicators.never-lit"
    }

    standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
    }

    valueTile("firmwareVersion", "device.firmwareVersion", width:2, height: 2, decoration: "flat", inactiveLabel: false) {
      state "default", label: '${currentValue}'
    }
    
    valueTile("driverVersion", "device.driverVersion", width:2, height:2, inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }

    standardTile("configure", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"configuration.configure", icon:"st.secondary.configure"
    }

    main "switch"
    details(["switch", "tapUp2", "tapUp3", "holdUp", "tapDown2", "tapDown3", "holdDown", "indicator", "firmwareVersion", "driverVersion", "refresh", "configure"])
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
      result = createEvent(value: description, descriptionText: description, isStateChange: true)
    }
  } else if (description != "updated") {
    def cmd = zwave.parse(description)
	
    if (cmd) {
      result = zwaveEvent(cmd)
      
      if (!result) {
        log.warning "Parse Failed and returned ${result} for command ${cmd}"
        result = createEvent(value: description, descriptionText: description)
      } else {
        // log.debug "PARSE: ${description}"
        // log.debug "RESULT: ${result}"
      }
    } else {
      log.info "zwave.parse() failed: ${description}"
      result = createEvent(value: description, descriptionText: description)
    }
  }
    
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  log.debug "BasicReport()"
  createEvent([name: "switch", value: cmd.value ? "on" : "off", type: "physical"])
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  log.debug "BasicSet()"
  createEvent([name: "switch", value: cmd.value ? "on" : "off", type: "physical"])
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  log.debug "SwitchBinaryReport()"
  createEvent([name: "switch", value: cmd.value ? "on" : "off", type: "digital"])
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
  def value = "when off"
  if (cmd.configurationValue[0] == 1) {value = "when on"}
  if (cmd.configurationValue[0] == 2) {value = "never"}
  createEvent([name: "indicatorStatus", value: value, display: false])
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  def result = []
  
  def manufacturerCode = String.format("%04X", cmd.manufacturerId)
  def productTypeCode = String.format("%04X", cmd.productTypeId)
  def productCode = String.format("%04X", cmd.productId)
  def wirelessConfig = "ZWP"
  
  result << createEvent(name: "ManufacturerCode", value: manufacturerCode)
  result << createEvent(name: "ProduceTypeCode", value: productTypeCode)
  result << createEvent(name: "ProductCode", value: productCode)
  result << createEvent(name: "WirelessConfig", value: wirelessConfig)

  if ( cmd.manufacturerId == 12 ) {
    updateDataValue("manufacturer", "HomeSeer")
  } else if ( cmd.manufacturerId == 388 ) {
    updateDataValue("manufacturer", "Dragon Tech Industrial, Ltd.")
  }

  def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
  updateDataValue("MSR", msr)
  updateDataValue("manufacturer", cmd.manufacturerName)
  if (!state.manufacturer) {
    state.manufacturer= cmd.manufacturerName
  }
  
  result << createEvent([name: "MSR", value: "$msr", descriptionText: "$device.displayName", isStateChange: false])
  result << createEvent([name: "Manufacturer", value: "${cmd.manufacturerName}", descriptionText: "$device.displayName", isStateChange: false])
  
  return result
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
  def text = "$device.displayName: firmware version: ${cmd.applicationVersion}.${cmd.applicationSubVersion}, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
  state.firmwareVersion = cmd.applicationVersion+'.'+cmd.applicationSubVersion
  updateDataValue("fw", state.firmwareVersion)
  createEvent([name: "firmwareVersion", value: "V ${state.firmwareVersion}", descriptionText: "$text", isStateChange: false])
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd) {
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  updateDataValue("FirmwareMdReport", firmware_report)
  createEvent([name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", isStateChange: false])
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  log.debug "ERROR: $cmd"
  createEvent(descriptionText: "$device.displayName command not implemented: $cmd", displayed: true)
}

def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
  log.debug "Hail()"
  createEvent([name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: true])
}

def on() {
  sendEvent(tapDown1Response("digital"))
  return commands([
    zwave.basicV1.basicSet(value: 0xFF),
    zwave.switchBinaryV1.switchBinaryGet()
  ])
}

def off() {
  log.debug "off()"
  return commands([
    zwave.basicV1.basicSet(value: 0x00),
    zwave.switchBinaryV1.switchBinaryGet()
  ])
}

def configure() {
  log.debug "configure()"
  setConfigured()
}

def poll() {
  return command(zwave.switchBinaryV1.switchBinaryGet())
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 * */
def ping() {
  refresh()
}

def refresh() {
  log.debug "refresh() called"
  response(commands([
    zwave.switchBinaryV1.switchBinaryGet()
  ]))
}

void indicatorWhenOn() {
  sendEvent(name: "indicatorStatus", value: "when on", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 3, size: 1).format()))
}

void indicatorWhenOff() {
  sendEvent(name: "indicatorStatus", value: "when off", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 3, size: 1).format()))
}

void indicatorNever() {
  sendEvent(name: "indicatorStatus", value: "never", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 3, size: 1).format()))
}

def invertSwitch(invert=true) {
  if (invert) {
    commands([
      zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 4, size: 1),
      zwave.configurationV1.configurationGet(parameterNumber: 4)
    ])
  }
  else {
    commands([
     zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 4, size: 1),
     zwave.configurationV1.configurationGet(parameterNumber: 4)
    ])
  }
}

def zwaveEvent(physicalgraph.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
  def result = []
  log.debug ("DeviceResetLocallyNotification()")
  
  result << createEvent([descriptionText: cmd.toString(), isStateChange: true, displayed: true])
  
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
      result=createEvent([name: "switch", value: "on", type: "physical"])
      break

      case 1:
      // Press Once
      result += createEvent(tapUp1Response("physical"))
      result += response("delay 100")
      result += createEvent([name: "switch", value: "on", type: "physical"])
      break
      case 2:
      // Hold
      result += createEvent(holdUpResponse("physical"))  
      result += response("delay 100")
      result += createEvent([name: "switch", value: "on", type: "physical"])    

      break
      case 3: 
      // 2 Times
      result=createEvent(tapUp2Response("physical"))
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
      result += createEvent(tapDown1Response("physical"))
      result += response("delay 100")
      result += createEvent([name: "switch", value: "off", type: "physical"])
      break

      case 1:
      // Press Once
      result=createEvent([name: "switch", value: "off", type: "physical"])
      break
      case 2:
      // Hold
      result += createEvent(holdDownResponse("physical"))
      result += response("delay 100")
      result += createEvent([name: "switch", value: "off", type: "physical"]) 
      break
      case 3: 
      // 2 Times
      result=createEvent(tapDown2Response("physical"))
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
  [name: "button", value: "pushed", data: [buttonNumber: "1"], descriptionText: "$device.displayName Tap-Up-2 (button 1) pressed", 
  isStateChange: true, type: "$buttonType"]
}

def tapDown2Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼▼")
  [name: "button", value: "pushed", data: [buttonNumber: "2"], descriptionText: "$device.displayName Tap-Down-2 (button 2) pressed", 
  isStateChange: true, type: "$buttonType"]
}

def tapUp3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲▲")
  [name: "button", value: "pushed", data: [buttonNumber: "3"], descriptionText: "$device.displayName Tap-Up-3 (button 3) pressed", 
  isStateChange: true, type: "$buttonType"]
}

def tapDown3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▼▼▼")
  [name: "button", value: "pushed", data: [buttonNumber: "4"], descriptionText: "$device.displayName Tap-Down-3 (button 4) pressed", 
  isStateChange: true, type: "$buttonType"]
}

def holdUpResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▲")
  [name: "button", value: "pushed", data: [buttonNumber: "5"], descriptionText: "$device.displayName Hold-Up (button 5) pressed", 
  isStateChange: true, type: "$buttonType"]
}

def holdDownResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▼")
  [name: "button", value: "pushed", data: [buttonNumber: "6"], descriptionText: "$device.displayName Hold-Down (button 6) pressed", 
  isStateChange: true, type: "$buttonType"]
}

def tapUp1Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲")
  [name: "button", value: "pushed", data: [buttonNumber: "7"], descriptionText: "$device.displayName Tap-Up-1 (button 7) pressed", isStateChange: true, type: "$buttonType"]
}

def tapDown1Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼")
  [name: "button", value: "pushed", data: [buttonNumber: "8"], descriptionText: "$device.displayName Tap-Down-1 (button 8) pressed", isStateChange: true, type: "$buttonType"]
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

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
  log.debug ("AssociationGroupingsReport() $cmd")
  def result = []

  result << createEvent([descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", isStateChange: true, displayed: true])

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupInfoReport cmd) {
  log.debug ("AssociationGroupingsReport() $cmd")
  def result = []

  result << createEvent([descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", isStateChange: true, displayed: true])

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
  log.debug "AssociationGroupNameReport() $cmd"
  def result = []

  result << createEvent(descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", displayed: true)

  return result
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
    }
    state.isAssociated = false
  } else {
    Boolean isStateChange = state.isAssociated ? true : false
    result << createEvent(name: "Associated",
                          value: "misconfigured",
                          descriptionText: "misconfigured group ${cmd.groupingIdentifier}",
                          displayed: true,
                          isStateChange: isStateChange)
  }
  
  if (state.isAssociated == false) {
    result << response(commands([ zwave.associationV2.associationSet(groupingIdentifier: getAssociationGroup(), nodeId: [zwaveHubNodeId]),
                                  zwave.associationV2.associationGet(groupingIdentifier: getAssociationGroup())
                                  ], 1000))
  }
    
  return result
}

def setConfigured() {
  [
    zwave.versionV1.versionGet(),
    zwave.associationV2.associationSet(groupingIdentifier: getAssociationGroup(), nodeId: [zwaveHubNodeId]),
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    zwave.firmwareUpdateMdV1.firmwareMdGet(),
    zwave.associationV2.associationGet(groupingIdentifier: getAssociationGroup()),
    zwave.switchBinaryV1.switchBinaryGet()
  ]
}

def installed() {
  log.debug ("installed()")
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)
  response(commands(setConfigured(), 2000))
}

def updated() {
  log.debug "updated()"
    
  // Device-Watch simply pings if no device events received for 32min(checkInterval)
  sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
  response(commands(setConfigured(), 2000))
  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed:true)
  state.driverVersion = getDriverVersion()
  switch (ledIndicator) {
    case "on":
      indicatorWhenOn()
      break
    case "off":
      indicatorWhenOff()
      break
    case "never":
      indicatorNever()
      break                                                                                             
    default:                                                                                              
      indicatorWhenOn()                                                                                 
      break                                                                                             
  }
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
