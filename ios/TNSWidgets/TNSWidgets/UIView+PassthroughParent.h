//
//  UIView+PassthroughParent.h
//  TNSWidgets
//
//  Created by Manol Donev on 21.08.18.
//  Copyright © 2018 Telerik A D. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface UIView (PassthroughParent)

- (BOOL) passthroughParent;
- (void) setPassthroughParent:(BOOL) passthroughParent;

@end
